package com.v2p.swp391.application.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BirdTypeRequest {
    @NotEmpty(message = "Bird Type name cannot be empty")
    private String name;
}
