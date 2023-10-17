package com.v2p.swp391.application.response;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class OrderPageResponse {
    private List<OrderResponse> orderResponses;
    private int totalPages;
}
