package com.v2p.swp391.application.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking")
public class Booking extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @Column(name = "phone_number",nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "shipping_address", length = 100)
    private String shippingAddress;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "status")
    private String status;

    @Column(name = "payment_deposit")
    private Float paymentDeposit;

    @Column(name = "total_payment")
    private Float totalPayment;
}
