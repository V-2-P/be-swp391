package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);

    @Query("SELECT od.bird, SUM(od.numberOfProducts) " +
            "FROM OrderDetail od " +
            "GROUP BY od.bird " +
            "ORDER BY SUM(od.numberOfProducts) DESC")
    List<Object[]> findBirdsByMostSoldQuantity();

    @Query("SELECT SUM(od.numberOfProducts) FROM OrderDetail od JOIN od.order o " +
            "WHERE od.bird.id = :birdId AND o.status <> 'cancelled'")
    Integer countSoldBirds(@Param("birdId") Long birdId);

}
