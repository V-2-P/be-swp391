package com.v2p.swp391.application.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalDashboardResponse {
    private long totalOrders;

    private long totalBookings;

    private long totalCustomerUsers;

    private long totalOrderProcessing;
}
