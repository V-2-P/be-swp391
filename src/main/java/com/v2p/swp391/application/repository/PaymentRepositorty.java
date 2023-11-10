package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositorty extends JpaRepository<Payment, String> {
    Payment findPaymentById(String id);
}
