package com.v2p.swp391.payment.impl;

import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.BookingRepository;
import com.v2p.swp391.application.repository.OrderRepository;
import com.v2p.swp391.application.repository.PaymentRepositorty;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.config.PaymentConfig;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public final BookingRepository bookingRepository;
    public final OrderRepository orderRepository;
    public final PaymentRepositorty paymentRepositorty;

    @Override
    public PaymentRespone createPayment(float total, PaymentForType payment, Long id) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        DecimalFormat df = new DecimalFormat("#000000");
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
            payment1 = new Payment(vnp_TxnRef, booking, null, total, "NCB", payment, false);
        }
        else if(payment.equals(PaymentForType.ORDER)){
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
            vnp_TxnRef = "OD" + df.format(id);
            payment1 = new Payment(vnp_TxnRef, null, order, total, "NCB", payment, false);
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

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+8"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
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
            if(currPayment.isStatus() == true){
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
        return paymentRespone;
    }


    @Override
    public Payment setData(String order, String id) {
        Payment payment = paymentRepositorty.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        payment.setStatus(true);

        if (order.equals(PaymentForType.DEPOSIT_BOOKING)){
            char[] idChar = new char[8];
            id.getChars(2, id.length(), idChar, 0);
            Long idInt = Long.parseLong(idChar.toString());
            Booking booking = bookingRepository.findById(idInt)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
            booking.setStatus(BookingStatus.Confirmed);

            bookingRepository.save(booking);
        }
        return paymentRepositorty.save(payment);
    }


}
