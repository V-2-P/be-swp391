package com.v2p.swp391.application.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private float amount;

    private String bankCode;

    @Enumerated(EnumType.STRING)
    private PaymentForType paymentForType;
//
//    @Enumerated(EnumType.STRING)
//    private RefundType  refundType;

    private boolean status;
}
