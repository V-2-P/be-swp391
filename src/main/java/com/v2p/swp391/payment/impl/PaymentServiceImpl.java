package com.v2p.swp391.payment.impl;

import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.service.impl.SendEmailServiceImpl;
import com.v2p.swp391.config.PaymentConfig;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public final BookingRepository bookingRepository;
    public final BookingDetailRepository bookingDetailRepository;
    public final OrderRepository orderRepository;
    public final PaymentRepositorty paymentRepositorty;
    public final SendEmailServiceImpl sendEmailService;
    private final BirdRepository birdRepository;

    @Override
    public PaymentRespone createPayment(float total, PaymentForType payment, Long id) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        DecimalFormat df = new DecimalFormat("#0000");
        String vnp_TxnRef = df.format(id);
        Payment payment1 = new Payment();
        if(payment.equals(PaymentForType.DEPOSIT_BOOKING) || payment.equals(PaymentForType.TOTAL_BOOKING)){
            if (payment.equals(PaymentForType.DEPOSIT_BOOKING)){
                vnp_TxnRef = "DB" + df.format(id);
            }
            if (payment.equals(PaymentForType.TOTAL_BOOKING)){
                vnp_TxnRef = "TB" + df.format(id);
            }
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
            payment1 = new Payment(LocalDateTime.now(), LocalDateTime.now(),vnp_TxnRef, booking, null, total, "NCB", payment, PaymentStatus.pending);
        }
        else if(payment.equals(PaymentForType.ORDER)){
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
            vnp_TxnRef = "OD" + df.format(id);
            payment1 = new Payment(LocalDateTime.now(), LocalDateTime.now(), vnp_TxnRef, null, order, total, "NCB", payment, PaymentStatus.pending);
        }



        String orderType = "other";
        String totalString = total + "";
        int index = totalString.indexOf(".");
        totalString = totalString.substring(0, index);
        Long amount = Long.parseLong(totalString) * 100;

//        DecimalFormat df = new DecimalFormat("#000000");
//        String vnp_TxnRef = df.format(id);

        String vnp_IpAddr = "14.191.95.88";
        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", payment.toString());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_OrderType", orderType);

        TimeZone vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(vietnamTimeZone);

        Date currentDate = new Date();
        String vnp_CreateDate = sdf.format(currentDate);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        Instant instant = currentDate.toInstant();
        Instant newInstant = instant.plus(Duration.ofMinutes(30));
        Date newDate = Date.from(newInstant);
        String vnp_ExpireDate = sdf.format(newDate.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;//
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;


        Payment currPayment =  paymentRepositorty.findPaymentById(vnp_TxnRef);
        if(currPayment != null){
            if(currPayment.getStatus() == PaymentStatus.success){
                return new PaymentRespone("Failed", "This payment is paid before!", "", currPayment);
            }
            else{
                currPayment.setUpdatedAt(LocalDateTime.now());
                paymentRepositorty.save(currPayment);
            }
        }
        else{
            paymentRepositorty.save(payment1);
        }

        PaymentRespone paymentRespone = new PaymentRespone("OK", "Successfully", paymentUrl, payment1);
//        this.sendRedirect(paymentUrl);
        return paymentRespone;
    }

    @Override
    public Payment setData(String id, Map field) {
        Payment payment = paymentRepositorty.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));

        if (id.contains("DB") || id.contains("TB")){
            Booking booking = bookingRepository.findById(payment.getBooking().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

            BookingDetail bookingDetail = bookingDetailRepository
                    .findById(payment.getBooking().getBookingDetail().getId())
                    .orElseThrow(()
                            -> new ResourceNotFoundException("Booking Detail", "ID", payment.getBooking().getBookingDetail().getId()));

            if (id.contains("DB")){
                booking.setStatus(BookingStatus.Confirmed);
                Float amount =  Float.parseFloat(field.get("vnp_Amount").toString()) / 100f;
                booking.setPaymentDeposit(amount);
//                bookingDetail.setStatus(BookingDetailStatus.In_Breeding_Progress);
            } else if(id.contains("TB")){
                bookingDetail.setStatus(BookingDetailStatus.Receiving_Confirm);
            }

            bookingDetailRepository.save(bookingDetail);
            bookingRepository.save(booking);
        } else if (id.contains("OD")) {
            Order order1 = orderRepository.findById(payment.getOrder().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
            order1.setStatus(OrderStatus.processing);
            orderRepository.save(order1);
        }

        String bankCode = (String) field.get("vnp_BankCode");
        String bankTranNo = (String) field.get("vnp_BankTranNo");
        String cardType = (String) field.get("vnp_CardType");
        String payDate = (String) field.get("vnp_PayDate");
        String tranNo = (String) field.get("vnp_TransactionNo");
        String transactionStatus = (String) field.get("vnp_TransactionStatus");
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setBankCode(bankCode);
        payment.setBankTranNo(bankTranNo);
        payment.setCardType(cardType);
        payment.setPayDate(payDate);
        payment.setTransactionNo(tranNo);
        payment.setTransactionStatus(transactionStatus);

        payment.setStatus(PaymentStatus.success);
        return paymentRepositorty.save(payment);
    }

    public Payment setCancel(String id){
        Payment payment = paymentRepositorty.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        payment.setStatus(PaymentStatus.fail);
        if(id.contains("DB")){
            Booking booking = bookingRepository.findById(payment.getBooking().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
            booking.setStatus(BookingStatus.Cancelled);
            Bird birdFather = booking.getBookingDetail().getFatherBird();
            Bird birdMother = booking.getBookingDetail().getMotherBird();
            birdMother.setQuantity(1);
            birdFather.setQuantity(1);

            birdRepository.save(birdMother);
            birdRepository.save(birdFather);
            bookingRepository.save(booking);
        } else if (id.contains("OD")) {
            Order order = orderRepository.findById(payment.getOrder().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
            order.setStatus(OrderStatus.cancelled);
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                Bird bird = orderDetail.getBird();
                int quantity = orderDetail.getNumberOfProducts();
                bird.setQuantity(bird.getQuantity() + quantity);
                birdRepository.save(bird);
            }
            orderRepository.save(order);

        }
        return paymentRepositorty.save(payment);
    }

//    public void sendPaymenOfUnpaidFee() throws UnsupportedEncodingException {
//        List<Payment> unpaidPaymentList = this.getUnpaidPayment();
//        LocalDateTime currentDate = LocalDateTime.now();
//        LocalDateTime cprDate = currentDate.minusHours(2);
//        for(Payment payment: unpaidPaymentList){
//            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
//            String createdAtString = sdf.format(payment.getCreatedAt());
//            String updatedAtString = sdf.format(payment.getUpdatedAt());
//            if(createdAtString.equals(updatedAtString)) {
//                if (payment.getUpdatedAt().isBefore(cprDate)) {
//                    float total;
//                    PaymentForType paymentForType;
//                    Long id;
//
//                    if (payment.getId().contains("OD")) {
//                        paymentForType = PaymentForType.ORDER;
//                        id = payment.getOrder().getId();
//                    } else if (payment.getId().contains("DB")) {
//                        paymentForType = PaymentForType.DEPOSIT_BOOKING;
//                        id = payment.getBooking().getId();
//                    } else {
//                        paymentForType = PaymentForType.TOTAL_BOOKING;
//                        id = payment.getBooking().getId();
//                    }
//
//                    total = payment.getAmount();
//                    PaymentRespone paymentRespone = this.createPayment(total, paymentForType, id);
//                    sendEmailService.sendMailPayment
//                            (payment.getBooking() != null ? payment.getBooking().getUser() : payment.getOrder().getUser()
//                                    , paymentRespone.getURL());
//                }
//            }
//        }
//    }

    private List<Payment> getUnpaidPayment(){
        List<Payment> unpaidPaymentList = new ArrayList<>();
        for(Payment payment: paymentRepositorty.findAll()){
            if(payment.getStatus() != PaymentStatus.success){
                unpaidPaymentList.add(payment);
            }
        }
        return unpaidPaymentList;
    }

    private void setCanceledForOutDatePayment(){
        List<Payment> unpaidPaymentList = this.getUnpaidPayment();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime cprDate = currentDate.minusDays(10);
        for(Payment payment: unpaidPaymentList){
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            String createdAtString = sdf.format(payment.getCreatedAt());
            String updatedAtString = sdf.format(payment.getUpdatedAt());
            if(!createdAtString.equals(updatedAtString)){
                if(payment.getBooking() != null){
                    Booking booking = payment.getBooking();
                    if(payment.getUpdatedAt().isBefore(cprDate)){
                        booking.setStatus(BookingStatus.Cancelled);
                        bookingRepository.save(booking);
                    }
                }
                else if (payment.getOrder() != null){
                    Order order = payment.getOrder();
                    if(payment.getUpdatedAt().isBefore(cprDate)){
                        order.setStatus(OrderStatus.cancelled);
                        orderRepository.save(order);
                    }
                }
            }

        }
    }

//    @Override
//    public RedirectView sendRedirect(String url) {
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl(url);
//        return redirectView.;
//    }


}
