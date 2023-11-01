package com.v2p.swp391.application.response;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class MonthlyDashboardResponse {

    private List<RevenueMonthlyResponse> weeklyRevenue;

    private long totalOrders;

    private long totalBookings;

    private long totalCustomerUsers;

    private double totalRevenue;
}
