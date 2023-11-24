package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.SocialAccount;
import com.v2p.swp391.application.model.SocialProvider;
import com.v2p.swp391.application.repository.SocialAccountRepository;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("${app.api.version.v1}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentServiceImpl paymentService;
    private final SocialAccountRepository socialAR;
    @GetMapping("/payment_infor")
    public CoreApiResponse<TransactionRespone> transaction(
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_TxnRef") String id,
            @RequestParam(value = "vnp_ResponseCode") String responeCode,
            HttpServletResponse response
    ) throws IOException {
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
        if(id.contains("OD")){
            response.sendRedirect("https://birdfarmshop.techx.id.vn/order");
        }
        else{
            response.sendRedirect("https://birdfarmshop.techx.id.vn/booking");
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

    @GetMapping("/vnpay/ipn")
    public ResponseEntity<VnpayResponse> handleIPN(

            HttpServletRequest request
            ){
        VnpayResponse vnp = new VnpayResponse();
        try {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = (String) params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0))
                {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType"))
            {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash"))
            {
                fields.remove("vnp_SecureHash");
            }
            // Check checksum
            String signValue = PaymentConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash))
            {

                boolean checkOrderId = true; // vnp_TxnRef exists in your database
                boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
                boolean checkOrderStatus = true; // PaymnentStatus = 0 (pending)

                SocialAccount sa = new SocialAccount();

                Random ran = new Random();

                sa.setSocialId(ran.toString());
                sa.setProvider(SocialProvider.FACEBOOK);

                socialAR.save(new SocialAccount());

                if(checkOrderId)
                {
                    if(checkAmount)
                    {
                        if (checkOrderStatus)
                        {
                            if ("00".equals(request.getParameter("vnp_ResponseCode")))
                            {

                                //Here Code update PaymnentStatus = 1 into your Database
                            }
                            else
                            {

                                // Here Code update PaymnentStatus = 2 into your Database
                            }
                            vnp.setMessage("Confirm Success");
                            vnp.setRspCode("00");
                        }
                        else
                        {
                            vnp.setMessage("Order already confirmed");
                            vnp.setRspCode("02");
                        }
                    }
                    else
                    {
                        vnp.setMessage("Invalid Amount");
                        vnp.setRspCode("04");
                    }
                }
                else
                {
                    vnp.setMessage("Order not Found");
                    vnp.setRspCode("01");
                }
            }
            else
            {
                vnp.setMessage("Invalid Checksum");
                vnp.setRspCode("97");
            }
        }catch (Exception e){
            vnp.setMessage("Unknow error");
            vnp.setRspCode("99");
        }
        return ResponseEntity.ok(vnp);
    }



}
