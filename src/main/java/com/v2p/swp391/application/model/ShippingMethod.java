package com.v2p.swp391.application.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipping_method")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "shipping_money")
    private float shippingMoney;
}
