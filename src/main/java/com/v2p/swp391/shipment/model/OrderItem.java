package com.v2p.swp391.shipment.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String name;
    private String code;
    private int quantity;
    private int price;
    private int weight;
    private int length;
    private int width;
    private int height;
}
