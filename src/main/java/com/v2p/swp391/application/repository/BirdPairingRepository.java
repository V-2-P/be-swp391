package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.BirdPairing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BirdPairingRepository extends JpaRepository<BirdPairing, Long> {
    @Query(value = "SELECT p.*\n" +
            "FROM booking b, booking_detail d, bird_pairing p, birds bi\n" +
            "WHERE d.booking_id = b.id AND p.booking_detail_id = d.id AND p.new_bird_id = bi.id\n" +
            "AND b.id = :id", nativeQuery = true)
    List<BirdPairing> getNewBirdByBookingId(@Param("id") Long id);
}
