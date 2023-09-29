package com.v2p.swp391.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class BirdSearchResponse {
    private List<BirdResponse> birds;
    private int totalPages;
}
