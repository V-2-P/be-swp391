package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.request.PaymentRequest;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.response.TransactionRespone;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.payment.impl.PaymentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("${app.api.version.v1}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @GetMapping("/payment_infor")
    public CoreApiResponse<TransactionRespone> transaction(
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_TxnRef") String id,
            @RequestParam(value = "vnp_ResponseCode") String responeCode
    ){
        TransactionRespone transactionRespone = new TransactionRespone();
        if(responeCode.equals("00")){
            transactionRespone.setStatus("OK");
            transactionRespone.setMessage("Successfully!");
            transactionRespone.setData("");
            transactionRespone.setPayment(paymentService.setData(order, id));
        } else {
            transactionRespone.setStatus("No");
            transactionRespone.setMessage("Failed!");
            transactionRespone.setData("");

        }
        return CoreApiResponse.success(transactionRespone, "Successfully!");
    }

//    @PostMapping("/create_refund")
//    public CoreApiResponse<PaymentRespone> createRefund(
//            @Valid @RequestBody RefundRequest request
//    ) throws UnsupportedEncodingException {
//        PaymentRespone paymentRespone = paymentService.createRefund(request);
//        return CoreApiResponse.success(paymentRespone, "Successfully");
//    }
}
