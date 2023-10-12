package com.v2p.swp391.application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "bird_id")
    private Bird bird;

    @Column(name = "number_of_products", nullable = false)
    private int numberOfProducts;

    @Column(name = "price", nullable = false)
    private Float price;

}
