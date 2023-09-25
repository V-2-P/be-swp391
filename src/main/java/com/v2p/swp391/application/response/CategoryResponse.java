package com.v2p.swp391.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.v2p.swp391.application.model.Category;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private String errors;

    @JsonProperty("category")
    private Category category;
}
