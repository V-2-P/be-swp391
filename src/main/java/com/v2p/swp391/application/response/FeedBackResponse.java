package com.v2p.swp391.application.response;

import com.v2p.swp391.application.model.BirdType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FeedBackResponse {
    private Long birdId;

    private int rating;

    private String comment;

    private BirdType birdType;
}
