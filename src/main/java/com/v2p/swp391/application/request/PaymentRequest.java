package com.v2p.swp391.application.request;

import com.v2p.swp391.application.model.PaymentForType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @Min(value = 10000, message = "The amount is higher than 10.000VND")
    private float amount;

    @NotNull(message = "Payment For is required!")
    private PaymentForType paymentForType;

    @Min(value = 1, message = "ID is positive!")
    private Long id;

}
