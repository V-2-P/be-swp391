package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.request.PaymentRequest;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.response.TransactionRespone;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.config.PaymentConfig;
import com.v2p.swp391.payment.impl.PaymentServiceImpl;
import com.v2p.swp391.payment.respond.VnpayResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("${app.api.version.v1}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceImpl paymentService;
    public final BookingRepository bookingRepository;
    public final OrderRepository orderRepository;
    public final PaymentRepositorty paymentRepositorty;
    public final BookingDetailRepository bookingDetailRepository;

//    @GetMapping("/payment_infor")
//    public CoreApiResponse<TransactionRespone> transaction(
//            @RequestParam(value = "vnp_OrderInfo") String order,
//            @RequestParam(value = "vnp_TxnRef") String id,
//            @RequestParam(value = "vnp_ResponseCode") String responeCode,
//            HttpServletResponse response
//    ) throws IOException {
//        TransactionRespone transactionRespone = new TransactionRespone();
//        if (responeCode.equals("00")) {
//            transactionRespone.setStatus("OK");
//            transactionRespone.setMessage("Successfully!");
//            transactionRespone.setData("");
//            transactionRespone.setPayment(paymentService.setData(id));
//        } else {
//            transactionRespone.setStatus("No");
//            transactionRespone.setMessage("Failed!");
//            transactionRespone.setData("");
//
//        }
//        if (id.contains("OD")) {
//            response.sendRedirect("https://birdfarmshop.techx.id.vn/order");
//        } else {
//            response.sendRedirect("https://birdfarmshop.techx.id.vn/booking");
//        }
//
//        return CoreApiResponse.success(transactionRespone, "Successfully!");
//    }

//    @PostMapping("/create_refund")
//    public CoreApiResponse<PaymentRespone> createRefund(
//            @Valid @RequestBody RefundRequest request
//    ) throws UnsupportedEncodingException {
//        PaymentRespone paymentRespone = paymentService.createRefund(request);
//        return CoreApiResponse.success(paymentRespone, "Successfully");
//    }

    @GetMapping("/vnpay/ipn")
    public ResponseEntity<VnpayResponse> handleIPN(

            HttpServletRequest request
    ) {
        VnpayResponse vnp = new VnpayResponse();
        try {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = (String) params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            // Check checksum
            String signValue = PaymentConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                boolean checkOrderId = true; // check order/booking có tồn tại trong database không
                boolean checkAmount = true; // check amount có bằng amount trong database không
                boolean checkOrderStatus = true; // kiểm tra xem order/booking có phải pending không

                String id = (String) fields.get("vnp_TxnRef");
                Long bookingID = Long.valueOf(id.substring(2));
                Payment payment = paymentRepositorty.findPaymentById(id);

                if (id.contains("DB") || id.contains("TB")) {
                    Booking booking = bookingRepository.findBookingById(bookingID);

                    checkOrderId = booking != null;
                    if(checkOrderId){
                        checkOrderStatus = payment.getBooking().getStatus().equals(BookingStatus.Pending);
                        if (id.contains("DB"))
                            checkAmount = payment.getAmount() == booking.getPaymentDeposit();
                        else if (id.contains("TB")) {
                            checkAmount = payment.getAmount() == booking.getTotalPayment() - booking.getPaymentDeposit();
                        }
                    }

                } else if (id.contains("OD")) {
                    Order order = orderRepository.findOrderById(bookingID);
                    checkOrderId = order != null;
                    if(checkOrderId){
                        checkAmount = payment.getAmount() == order.getTotalPayment();
                        checkOrderStatus = payment.getOrder().getStatus().equals(OrderStatus.pending);
                    }

                }

                //Xử lí logic ở đây cập nhật vào database

                if (checkOrderId) {
                    if (checkAmount) {
                        if (checkOrderStatus) {
                            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                                //Xử lí logic thành công

                                paymentService.setData(id, fields);
                            } else {
                                paymentService.setCancel(id);
                                //Xử lí logic thất bại rồi lưu vào database
                            }
                            vnp.setMessage("Confirm Success");
                            vnp.setRspCode("00");
                        } else {
                            vnp.setMessage("Order already confirmed");
                            vnp.setRspCode("02");
                        }
                    } else {
                        vnp.setMessage("Invalid Amount");
                        vnp.setRspCode("04");
                    }
                } else {
                    vnp.setMessage("Order not Found");
                    vnp.setRspCode("01");
                }
            } else {
                vnp.setMessage("Invalid Checksum");
                vnp.setRspCode("97");
            }
        } catch (Exception e) {
            vnp.setMessage("Unknow error");
            vnp.setRspCode("99");
        }
        return ResponseEntity.ok(vnp);
    }


}
