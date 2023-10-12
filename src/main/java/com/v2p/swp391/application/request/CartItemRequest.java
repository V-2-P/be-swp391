package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    @JsonProperty("bird_id")
    private Long productId;

    @JsonProperty("quantity")
    private int quantity;
}
