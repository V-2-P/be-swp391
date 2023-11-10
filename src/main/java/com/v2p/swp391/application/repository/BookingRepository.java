package com.v2p.swp391.application.repository;

import com.v2p.swp391.application.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT COUNT(b) FROM Booking b WHERE YEAR(b.createdAt) = :year AND MONTH(b.createdAt) = :month")
    long countBookingsByYearAndMonth(@Param("year") int year, @Param("month") int month);
    long count();
    @Query("SELECT COUNT(b) FROM Booking b WHERE DATE(b.createdAt) = CURRENT_DATE")
    long countBookingsForCurrentDate();

    List<Booking> findByUserId(Long id);

    Booking findBookingById(Long id);
}
