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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final OrderRepository orderRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;



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
        orderPage = orderRepository.findAll( pageRequest);
        return orderPage.map(orderMapper::toResponseDashboard);
    }


    @Override
    public List<RevenueMonthlyResponse> calculateRevenueByMonth(String month) {
        String sql = "SELECT DATE_FORMAT(transaction_time, '%Y-%m') AS period, " +
                "SUM(total_payment) AS total_payment " +
                "FROM (" +
                "    SELECT booking_time AS transaction_time, total_payment " +
                "    FROM booking " +
                "    WHERE status = 'DELIVERED' " +
                "    AND DATE_FORMAT(booking_time, '%Y-%m') = :month " +
                "    UNION ALL " +
                "    SELECT order_date AS transaction_time, total_payment " +
                "    FROM orders " +
                "    WHERE status = 'DELIVERED' " +
                "    AND DATE_FORMAT(order_date, '%Y-%m') = :month " +
                ") AS combined_transactions " +
                "GROUP BY DATE_FORMAT(transaction_time, '%Y-%m') " +
                "ORDER BY period";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("month", month);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            RevenueMonthlyResponse result = new RevenueMonthlyResponse();
            result.setPeriod(rs.getString("period"));
            result.setTotalPayment(rs.getFloat("total_payment"));
            return result;
        });
    }



    @Override
    public List<RevenueDailyResponse> calculateRevenueByDay(String day) {
        String sql = "SELECT DATE_FORMAT(transaction_time, '%Y-%m-%d') AS day, " +
                "SUM(total_payment) AS total_payment " +
                "FROM (" +
                "    SELECT booking_time AS transaction_time, total_payment " +
                "    FROM booking " +
                "    WHERE status = 'DELIVERED' AND DATE_FORMAT(booking_time, '%Y-%m-%d') = :day " +
                "    UNION ALL " +
                "    SELECT order_date AS transaction_time, total_payment " +
                "    FROM orders " +
                "    WHERE status = 'DELIVERED' AND DATE_FORMAT(order_date, '%Y-%m-%d') = :day " +
                ") AS combined_transactions " +
                "GROUP BY DATE_FORMAT(transaction_time, '%Y-%m-%d') " +
                "ORDER BY day";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("day", day);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            RevenueDailyResponse result = new RevenueDailyResponse();
            result.setDay(rs.getString("day"));
            result.setTotalPayment(rs.getFloat("total_payment"));
            return result;
        });
    }

    @Override
    public TotalDashboardResponse getDashboardTotals() {
        long totalOrders = orderRepository.count();
        long totalBookings = bookingRepository.count();
        long totalCustomerUsers = userRepository.countCustomers();
        long totalOrderProcessing= orderRepository.countByStatusConfirm();
        return new TotalDashboardResponse(totalOrders, totalBookings, totalCustomerUsers,totalOrderProcessing);
    }
}






