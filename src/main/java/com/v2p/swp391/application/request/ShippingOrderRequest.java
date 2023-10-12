package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingOrderRequest {

    @NotBlank(message = "Shipping method is required")
    @JsonProperty("shipping_method")
    private String shippingMethod;


    @JsonProperty("tracking_number")
    private String trackingNumber;


}
