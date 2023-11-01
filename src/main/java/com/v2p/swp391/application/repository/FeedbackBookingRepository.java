package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.FeedbackBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackBookingRepository extends JpaRepository<FeedbackBooking, Long> {
}
