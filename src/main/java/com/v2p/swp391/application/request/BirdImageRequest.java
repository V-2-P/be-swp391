package com.v2p.swp391.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class BirdImageRequest {
    @Min(value = 1, message = "Bird's ID must be > 0")
    private Long productId;

    @Size(min = 5, max = 200)
    @JsonProperty("image_url")
    private String imageUrl;
}
