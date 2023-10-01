package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.BookingHttpMapper;
import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.repository.BookingRepository;
import com.v2p.swp391.application.request.BookingRequest;
import com.v2p.swp391.application.service.BookingService;
import com.v2p.swp391.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingHttpMapper bookingMapper;

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find booking with id: " + id));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingRequest bookingRequest) {
        Booking existingBooking = getBookingById(bookingId);
//        existingBooking.setUser(booking.getUser());
//        existingBooking.setFullName(booking.getFullName());
//        existingBooking.setPhoneNumber(booking.getPhoneNumber());
//        existingBooking.setShippingAddress(booking.getShippingAddress());
//        existingBooking.setPaymentMethod(booking.getPaymentMethod());
//        existingBooking.setManager(booking.getManager());
//        existingBooking.setStatus(booking.getStatus());
//        existingBooking.setPaymentDeposit(booking.getPaymentDeposit());
//        existingBooking.setTotalPayment(booking.getTotalPayment());
        bookingMapper.updateBookingInfoFromRequest(bookingRequest, existingBooking);
        return bookingRepository.save(existingBooking);
    }

    @Override
    public Booking deleteBooking(Long id) {
        Booking existingBooking = getBookingById(id);
        bookingRepository.deleteById(id);
        return existingBooking;
    }
}
