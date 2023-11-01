package com.v2p.swp391.application.response;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class PageDashboardResponse {
    private List<OrderDashBoardResponse> orderResponses;
    private int totalPages;
}
