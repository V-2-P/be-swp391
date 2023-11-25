package com.v2p.swp391.shipment.request;

import com.v2p.swp391.shipment.CreateStrategyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Order Id is required")
    private Long id;

    @Pattern(regexp = "ORDER|BOOKING", message = "Invalid strategy type")
    private String strategyType;

    private String note;
}
