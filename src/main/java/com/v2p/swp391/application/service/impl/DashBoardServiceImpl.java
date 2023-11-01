package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.OrderHttpMapper;
import com.v2p.swp391.application.model.Bird;
import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.repository.BookingRepository;
import com.v2p.swp391.application.repository.OrderDetailRepository;
import com.v2p.swp391.application.repository.OrderRepository;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.response.*;
import com.v2p.swp391.application.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final OrderRepository orderRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    private final OrderHttpMapper orderMapper;

    public List<BirdBestSellerResponse> getMostSoldBirds() {
        List<Object[]> result = orderDetailRepository.findBirdsByMostSoldQuantity();
        List<BirdBestSellerResponse> bestSellers = new ArrayList<>();

        for (Object[] row : result) {
            if (row.length == 2) {
                Bird bird = (Bird) row[0];
                Long totalSold = (Long) row[1];

                BirdBestSellerResponse response = new BirdBestSellerResponse(bird.getName(), totalSold); // Đảm bảo bạn truy cập tên của Bird một cách chính xác
                bestSellers.add(response);
            }
        }
        return bestSellers;
    }

    @Override
    public Page<OrderDashBoardResponse> getLastOrder(PageRequest pageRequest) {
        Page<Order> orderPage;
        orderPage = orderRepository.findAll(pageRequest);
        return orderPage.map(orderMapper::toResponseDashboard);
    }


    @Override
    public List<RevenueMonthlyResponse> calculateYearlyRevenue(int year) {
        List<RevenueMonthlyResponse> yearlyRevenue = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            String period = String.format("%d-%02d", year, month);
            float totalPayment = calculateMonthlyRevenue(year, month); // Gọi hàm tính toán doanh thu cho tháng
            yearlyRevenue.add(new RevenueMonthlyResponse(period, totalPayment));
        }
        return yearlyRevenue;
    }
    @Override
    public float calculateMonthlyRevenue(int year, int month) {
        String sql = "SELECT SUM(total_payment) AS total_payment " +
                "FROM (" +
                "    SELECT updated_at AS transaction_time, total_payment " +
                "    FROM booking " +
                "    WHERE status = 'DELIVERED' " +
                "    AND YEAR(updated_at) = :year " +
                "    AND MONTH(updated_at) = :month " +
                "    UNION ALL " +
                "    SELECT updated_at AS transaction_time, total_payment " +
                "    FROM orders " +
                "    WHERE status = 'DELIVERED' " +
                "    AND YEAR(updated_at) = :year " +
                "    AND MONTH(updated_at) = :month " +
                ") AS combined_transactions";

        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);

        Float totalPayment = namedParameterJdbcTemplate.queryForObject(sql, params, Float.class);

        return totalPayment != null ? totalPayment : 0.0f;
    }


    @Override
    public List<RevenueDayResponse> calculateWeeklyRevenue() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<RevenueDayResponse> weeklyRevenue = new ArrayList<>();

        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            String period = date.toString();
            double totalPayment = calculateTotalPaymentForDate(date);
            weeklyRevenue.add(new RevenueDayResponse(period, totalPayment));
        }

        return weeklyRevenue;
    }

    @Override
    public double calculateTotalPaymentForDate(LocalDate date) {
        String sql = "SELECT SUM(total_payment) " +
                "FROM (" +
                "    SELECT total_payment " +
                "    FROM booking " +
                "    WHERE status = 'DELIVERED' " +
                "    AND DATE(updated_at) = :date " +
                "    UNION ALL " +
                "    SELECT total_payment " +
                "    FROM orders " +
                "    WHERE status = 'DELIVERED' " +
                "    AND DATE(updated_at) = :date " +
                ") AS combined_transactions";

        Map<String, Object> params = new HashMap<>();
        params.put("date", date);

        Double result = namedParameterJdbcTemplate.queryForObject(sql, params, Double.class);

        return result != null ? result : 0.0;
    }


    @Override
    public long countBookingsInCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        return bookingRepository.countBookingsByYearAndMonth(year, month);
    }

    @Override
    public long countBookingsForCurrentDate() {
        return bookingRepository.countBookingsForCurrentDate();
    }

    @Override
    public long countOrdersInCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        return orderRepository.countOrdersByYearAndMonth(year, month);
    }

    @Override
    public long countOrdersForCurrentDate() {
        return orderRepository.countOrdersForCurrentDate();
    }

    @Override
    public long totalUser() {
        return userRepository.countCustomers();
    }
}






