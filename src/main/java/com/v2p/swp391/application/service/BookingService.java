package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.request.BookingRequest;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    Booking getBookingById(Long id);
    List<Booking> getAllBookings();
    Booking updateBooking(Long bookingId, BookingRequest bookingRequest);
    Booking deleteBooking(Long id);
}
