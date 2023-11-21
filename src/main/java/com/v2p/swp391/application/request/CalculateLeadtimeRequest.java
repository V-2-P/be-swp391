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
public class CalculateLeadtimeRequest {
    private int from_district_id;

    private String from_ward_code;

    @NotNull(message = "to_district_id is required")
    private int to_district_id;

    @NotBlank(message = "to_ward_code is required")
    private String to_ward_code;

    private int service_id;
}
