package com.v2p.swp391.application.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    @NotNull
    private Long orderId;

    @NotNull
    private List<FeedbackBirdRequest> birdFeedbacks;

}
