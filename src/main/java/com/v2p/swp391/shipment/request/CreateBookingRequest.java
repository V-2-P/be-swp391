package com.v2p.swp391.shipment.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    @NotNull(message = "Booking Id is required")
    private Long id;
}
