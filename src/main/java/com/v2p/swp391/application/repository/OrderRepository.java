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
    List<Order> findByUserId(Long userId);


    @Query("SELECT o FROM Order o WHERE (:status is null OR o.status = :status)"+
            "AND (:keyword IS NULL OR :keyword = '' OR o.fullName LIKE %:keyword% OR o.id LIKE %:keyword%)")

    Page<Order> findByStatus(@Param("status") OrderStatus status,
                             @Param("keyword") String keyword,
                             Pageable pageable);


    List<Order> findByStatus(OrderStatus status);

    long count();

    @Query("SELECT COUNT(o) FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    long countOrdersByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(o) FROM Order o WHERE DATE(o.orderDate) = CURRENT_DATE")
    long countOrdersForCurrentDate();

    @Query("SELECT o FROM Order o WHERE o.expectedDate < :date AND o.status <> 'delivered'")
    List<Order> findOrdersForStatusUpdate(LocalDate date);


}
