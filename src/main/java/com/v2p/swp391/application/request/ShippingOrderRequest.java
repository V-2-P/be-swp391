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

    @NotBlank(message = "Tracking  is required")
    @JsonProperty("trackingNumber")
    private String trackingNumber;


}
