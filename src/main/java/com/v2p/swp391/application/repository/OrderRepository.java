package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);


    @Query("SELECT o FROM Order o WHERE (:status is null OR o.status = :status)")
    List<Order> findByStatus(@Param("status") OrderStatus status);



}
