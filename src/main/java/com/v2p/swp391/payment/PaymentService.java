package com.v2p.swp391.payment;

import com.v2p.swp391.application.model.Payment;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.model.PaymentForType;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface PaymentService {
    public PaymentRespone createPayment(float total, PaymentForType payment, Long id) throws UnsupportedEncodingException;
    public Payment setData(String id, Map field);
}
