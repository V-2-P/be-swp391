package com.v2p.swp391.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackBookingRequest{
    @NotNull(message = "Booking ID is required!")
    private Long bookingId;

    @NotBlank(message = "Comment is required!")
    @Length(min = 20)
    private String comment;

    @NotNull(message = "Rating is required")
    @Range(min = 1, max = 5)
    private int rating;
}
