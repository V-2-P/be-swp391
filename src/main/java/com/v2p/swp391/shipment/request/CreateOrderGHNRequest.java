package com.v2p.swp391.shipment.request;

import com.v2p.swp391.shipment.model.OrderItem;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderGHNRequest {
    private int payment_type_id;
    private String note;
    private String required_note;
    private String from_name;
    private String from_phone;
    private String from_address;
    private String from_ward_name;
    private String from_district_name;
    private String from_province_name;
    private String to_name;
    private String to_phone;
    private String to_address;
    private String to_ward_code;
    private int to_district_id;
    private int cod_amount;
    private String content;
    private int service_type_id;
    private int service_id;
    private List<OrderItem> items;
    private int weight;
    private int length;
    private int width;
    private int height;
}
