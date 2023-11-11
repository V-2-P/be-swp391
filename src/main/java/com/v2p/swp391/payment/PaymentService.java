package com.v2p.swp391.payment;

import com.v2p.swp391.application.model.Payment;
import com.v2p.swp391.application.response.PaymentRespone;
import com.v2p.swp391.application.model.PaymentForType;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    public PaymentRespone createPayment(float total, PaymentForType payment, Long id) throws UnsupportedEncodingException;
    public Payment setData(String order, String id);
}
