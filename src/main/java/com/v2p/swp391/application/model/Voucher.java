package com.v2p.swp391.application.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voucher extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int discount;

    @Column(name = "name", length = 100)
    private String name;

    private int amount;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    private boolean status;
}
