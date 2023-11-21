package com.v2p.swp391.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateFeeRequest {

    @NotNull(message = "to_district_id is required")
    private int to_district_id;

    @NotBlank(message = "to_ward_code is required")
    private String to_ward_code;

    private int service_type_id;
    private int weight;
    private int length;
    private int width;
    private int height;
}
