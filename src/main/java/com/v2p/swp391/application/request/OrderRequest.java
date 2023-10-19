package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @JsonProperty("userId")
    @Min(value = 1, message = "User's ID must be > 0")
    private Long userId;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, message = "Phone number must be 10 characters")
    private String phoneNumber;

    private String note;

    @JsonProperty("shippingAddress")
    private String shippingAddress;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @Min(value = 1, message = "Voucher ID must be > 0")
    @JsonProperty("voucher")
    private String voucherId;

    @JsonProperty("cartItems")
    private List<CartItemRequest> cartItems;

}




