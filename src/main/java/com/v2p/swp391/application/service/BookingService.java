package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.model.BookingStatus;
import com.v2p.swp391.application.request.BookingRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, BookingDetail bookingDetail);
    Booking getBookingById(Long id);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByUserId(Long id);
    Booking updateStatusBooking(Long bookingId, BookingStatus status);
    Booking updateTotalPaymentBooking(Long bookingId, Float total);
    Booking updateTimeBooking(Long bookingId, String dateString);
    Booking deleteBooking(Long id);
}
