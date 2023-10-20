package com.v2p.swp391.application.service;


import com.v2p.swp391.application.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DashBoardService {
    List<BirdBestSellerResponse> getMostSoldBirds();

    Page<OrderDashBoardResponse> getLastOrder(PageRequest pageRequest);

    List<RevenueMonthlyResponse> calculateRevenueByMonth(String month);

    List<RevenueDailyResponse> calculateRevenueByDay(String day);

    TotalDashboardResponse getDashboardTotals();
    }
