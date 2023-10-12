package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.request.BookingRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, BookingDetail bookingDetail);
    Booking getBookingById(Long id);
    List<Booking> getAllBookings();
    Booking updateStatusBooking(Long bookingId, String status);
    Booking updateTotalPaymentBooking(Long bookingId, Float total);
    Booking updateTimeBooking(Long bookingId, String dateString);
    Booking deleteBooking(Long id);
}