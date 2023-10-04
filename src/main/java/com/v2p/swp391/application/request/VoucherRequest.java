package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRequest {
    @NotNull(message = "Discount cannot be empty")
    @Min(value = 5, message = "Discount must be at least 5")
    @Max(value = 50, message = "Discount must be at most 50")
    private int discount;

    @NotEmpty(message = "Voucher name cannot be empty")
    @Size(min = 5, max = 50, message = "Name must be between 10 and 50 characters")
    private String name;

    private String description;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

    private boolean status;

}
