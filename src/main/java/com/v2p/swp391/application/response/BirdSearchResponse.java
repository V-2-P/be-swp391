package com.v2p.swp391.application.response;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class BirdSearchResponse {
    private List<BirdResponse> birds;
    private int totalPages;
}
