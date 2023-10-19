package com.v2p.swp391.application.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FeedbackBirdRequest {
    @NotNull
    private Long birdId;

    @Min(value = 1, message = "Rating must be >0")
    @Max(value=5,message = "Rating must be <5")
    private int rating;

    @Size(min = 1, max = 200, message = "Comment must be between 1 and 200 characters")
    private String comment;
}
