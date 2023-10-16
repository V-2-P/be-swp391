package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailRequest {
    @NotNull(message = "Bird type id is required")
    private Long birdTypeId;

    @NotNull(message = "Father bird ID is required")
    private Long fatherBirdId;

    @NotNull(message = "Mother bird ID is required")
    private Long motherBirdId;
}
