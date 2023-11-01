package com.v2p.swp391.application.response;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class DailyDashboardResponse {
    private List<RevenueDayResponse> weeklyRevenue;

    private long totalOrders;

    private long totalBookings;

    private long totalCustomerUsers;

    private double totalRevenue;

}
