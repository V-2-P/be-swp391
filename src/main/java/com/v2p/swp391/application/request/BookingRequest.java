package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.model.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Min(value = 1, message = "User's ID must be >0")
    private Long userId;

    @NotBlank(message = "Full name is  required!")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, message = "Phone number must be at least 5 characters")
    private String phoneNumber;

    @NotBlank(message = "Delivery address is required")
    private String shippingAddress;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Booking detail is required")
    private BookingDetailRequest bookingDetailRequest;
}
