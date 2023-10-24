package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.response.*;
import com.v2p.swp391.application.service.DashBoardService;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/revenue")
    public CoreApiResponse <?> getRevenue(@RequestParam("search") String search) {
        LocalDate currentDate = LocalDate.now();
        if (search.equals("daily")) {
            List<RevenueDayResponse> weeklyRevenue = dashBoardService.calculateWeeklyRevenue();
            DailyDashboardResponse dailyDashboard = new DailyDashboardResponse();
            dailyDashboard.setWeeklyRevenue(weeklyRevenue);
            dailyDashboard.setTotalBookings(dashBoardService.countBookingsForCurrentDate());
            dailyDashboard.setTotalCustomerUsers(dashBoardService.totalUser());
            dailyDashboard.setTotalOrders(dashBoardService.countOrdersForCurrentDate());
            dailyDashboard.setTotalRevenue(dashBoardService.calculateTotalPaymentForDate(LocalDate.now()));
            return CoreApiResponse.success(dailyDashboard);
        } else if (search.equals("month")) {
            int year = currentDate.getYear();
            int month = currentDate.getMonthValue();
            List<RevenueMonthlyResponse> yearlyRevenue = dashBoardService.calculateYearlyRevenue(year);
            MonthlyDashboardResponse monthlyDashboardResponse = new MonthlyDashboardResponse();
            monthlyDashboardResponse.setWeeklyRevenue(yearlyRevenue);
            monthlyDashboardResponse.setTotalBookings(dashBoardService.countBookingsInCurrentMonth());
            monthlyDashboardResponse.setTotalCustomerUsers(dashBoardService.totalUser());
            monthlyDashboardResponse.setTotalOrders(dashBoardService.countOrdersInCurrentMonth());
            monthlyDashboardResponse.setTotalRevenue(dashBoardService.calculateMonthlyRevenue(year, month));
            return CoreApiResponse.success(monthlyDashboardResponse);

        }else{
            throw new AppException(HttpStatus.BAD_REQUEST,"Search must be 'daily' or 'month'");
        }
    }
}
