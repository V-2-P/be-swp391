package com.v2p.swp391.application.service;


import com.v2p.swp391.application.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

public interface DashBoardService {
    List<BirdBestSellerResponse> getMostSoldBirds();

    Page<OrderDashBoardResponse> getLastOrder(PageRequest pageRequest);

    List<RevenueMonthlyResponse> calculateYearlyRevenue(int year);

    List<RevenueDayResponse> calculateWeeklyRevenue();

    long countBookingsInCurrentMonth();

    long countBookingsForCurrentDate();

    long countOrdersInCurrentMonth();

    long countOrdersForCurrentDate();

    long totalUser();

    float calculateMonthlyRevenue(int year, int month);

    double calculateTotalPaymentForDate(LocalDate date);

    }
