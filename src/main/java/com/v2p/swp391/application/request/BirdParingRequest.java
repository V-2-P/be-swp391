package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirdParingRequest {
    @JsonProperty("bookingDetailId")
    @NotNull(message = "New Bird Id is required!")
    private Long bookingDetailId;
}
