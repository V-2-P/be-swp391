package com.v2p.swp391.application.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RevenueRepository {
    @Query("SELECT DATE_FORMAT(b.bookingTime, '%Y-%m') AS period, SUM(b.totalPayment) " +
            "FROM Booking b " +
            "WHERE b.status = 'DELIVERED' " +
            "AND DATE_FORMAT(b.bookingTime, '%Y-%m') = :month " +
            "GROUP BY DATE_FORMAT(b.bookingTime, '%Y-%m') " +
            "UNION " +
            "SELECT DATE_FORMAT(o.orderDate, '%Y-%m') AS period, SUM(o.totalPayment) " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' " +
            "AND DATE_FORMAT(o.orderDate, '%Y-%m') = :month " +
            "GROUP BY DATE_FORMAT(o.orderDate, '%Y-%m')")
    List<Object[]> calculateRevenueByMonth(@Param("month") String month);

    @Query("SELECT DATE_FORMAT(b.bookingTime, '%Y-%m-%d') AS period, SUM(b.totalPayment) " +
            "FROM Booking b " +
            "WHERE b.status = 'DELIVERED' " +
            "AND DATE_FORMAT(b.bookingTime, '%Y-%m-%d') = :day " +
            "GROUP BY DATE_FORMAT(b.bookingTime, '%Y-%m-%d') " +
            "UNION " +
            "SELECT DATE_FORMAT(o.orderDate, '%Y-%m-%d') AS period, SUM(o.totalPayment) " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' " +
            "AND DATE_FORMAT(o.orderDate, '%Y-%m-%d') = :day " +
            "GROUP BY DATE_FORMAT(o.orderDate, '%Y-%m-%d')")
    List<Object[]> calculateRevenueByDay(@Param("day") String day);
}
