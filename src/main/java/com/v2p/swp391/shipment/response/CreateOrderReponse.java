package com.v2p.swp391.shipment.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderReponse {
    private String order_code;
    private String sort_code;
    private String trans_type;
    private String ward_encode;
    private String district_encode;
    private Fee fee;
    private int total_fee;
    private Date expected_delivery_time;
}
