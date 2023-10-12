package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);


    @Query("SELECT o FROM Order o WHERE :status IS NULL OR o.status LIKE %:status%")
    Page<Order> searchOrders(@Param("status") String status, Pageable pageable);



}
