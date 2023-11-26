package com.v2p.swp391.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment extends BaseEntity{
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private float amount;

    private String bankCode;

    private String BankTranNo;

    private String CardType;

    private String PayDate;

    private String TransactionNo;

    private String TransactionStatus;

    @Enumerated(EnumType.STRING)
    private PaymentForType paymentForType;
//
//    @Enumerated(EnumType.STRING)
//    private RefundType  refundType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment(LocalDateTime createdAt, LocalDateTime updatedAt, String id, Booking booking, Order order, float amount, String bankCode, PaymentForType paymentForType, PaymentStatus status) {
        super(createdAt, updatedAt);
        this.id = id;
        this.booking = booking;
        this.order = order;
        this.amount = amount;
        this.bankCode = bankCode;
        this.paymentForType = paymentForType;
        this.status = status;
    }
}
