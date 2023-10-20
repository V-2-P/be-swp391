package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.response.*;
import com.v2p.swp391.application.service.DashBoardService;
import com.v2p.swp391.common.api.CoreApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("${app.api.version.v1}/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashBoardService dashBoardService;

    @GetMapping("/order")
    public CoreApiResponse<PageDashboardResponse> getListOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        Page<OrderDashBoardResponse> orderPage = dashBoardService.getLastOrder(pageRequest);
        int totalPages= orderPage.getTotalPages();
        List<OrderDashBoardResponse> orders = orderPage.getContent();
        PageDashboardResponse orderPageResponse= new PageDashboardResponse();
        orderPageResponse.setTotalPages(totalPages);
        orderPageResponse.setOrderResponses(orders);
        return CoreApiResponse.success(orderPageResponse);
    }

    @GetMapping("/bestseller")
    public CoreApiResponse<List<BirdBestSellerResponse>> getMostSoldBirds() {
        List<BirdBestSellerResponse> bestSellers = dashBoardService.getMostSoldBirds();
        return CoreApiResponse.success(bestSellers);
    }

    @GetMapping("/revenue/monthly")
    public CoreApiResponse<List<RevenueMonthlyResponse>> calculateRevenueByMonth(@RequestParam("month") String month) {
        List<RevenueMonthlyResponse> revenueByMonth = dashBoardService.calculateRevenueByMonth(month);
        return CoreApiResponse.success(revenueByMonth);
    }

    @GetMapping("/revenue/daily")
    public CoreApiResponse<List<RevenueDailyResponse>> calculateRevenueByDay(@RequestParam("day") String day) {
        List<RevenueDailyResponse> revenueByDay = dashBoardService.calculateRevenueByDay(day);
        return CoreApiResponse.success(revenueByDay);
    }

    @GetMapping("/totals")
    public CoreApiResponse<TotalDashboardResponse> getTotals() {
        TotalDashboardResponse response = dashBoardService.getDashboardTotals();
        return CoreApiResponse.success(response);
    }


}
