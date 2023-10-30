package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId, Sort sort);


    @Query("SELECT o FROM Order o WHERE (:status is null OR o.status = :status)")
    Page<Order> findByStatus(@Param("status") OrderStatus status, Pageable pageable);

    long count();

    @Query("SELECT COUNT(o) FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    long countOrdersByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(o) FROM Order o WHERE DATE(o.orderDate) = CURRENT_DATE")
    long countOrdersForCurrentDate();



}
