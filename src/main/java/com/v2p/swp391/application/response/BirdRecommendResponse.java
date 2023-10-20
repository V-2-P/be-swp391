package com.v2p.swp391.application.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BirdRecommendResponse {

    private List<BirdResponse> bestSellers;

    private List<BirdResponse> top20;
}
