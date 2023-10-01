package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.v2p.swp391.application.model.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @JsonProperty("user_id")
    @Min(value = 1, message = "User's ID must be >0")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, message = "Phone number must be at least 5 characters")
    private String phoneNumber;

    @DateTimeFormat
    @JsonProperty("booking_time")
    private Date bookingTime;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("manager_id")
    @Min(value = 1, message = "Manager's ID must be >0")
    private Long managerId;

    @JsonProperty("payment_deposit")
    private Float paymentDeposit;

    @JsonProperty("total_payment")
    @Min(value = 0, message = "Total money must be >0")
    private Float totalPayment;


}
