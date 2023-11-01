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
    @Min(value = 1000, message = "Discount must be at least 1000")
    private float discount;

    @NotEmpty(message = "Voucher name cannot be empty")
    @Size(min = 5, max = 50, message = "Name must be between 10 and 50 characters")
    private String name;

    private String description;

    @JsonProperty("expirationDate")
    private LocalDate expirationDate;

    @JsonProperty("startDate")
    private LocalDate startDate;

    @Min(value = 1, message = "Amount must be at least 1")
    private int amount;

    @JsonProperty("minValue")
    @NotNull(message = "minValue cannot be empty")
    private float minValue;

    @NotEmpty(message = "Voucher code cannot be empty")
    @Size(min = 1, max = 20, message = "Code must be between 1 and 20 characters")
    private String code;

}

