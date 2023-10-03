package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
