package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethodRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("shippingMoney")
    private float shippingMoney;
}
