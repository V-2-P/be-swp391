package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.model.BookingStatus;
import com.v2p.swp391.application.model.PaymentForType;
import com.v2p.swp391.application.request.PaymentRequest;
import com.v2p.swp391.application.response.BookingResponse;
import com.v2p.swp391.application.response.PaymentRespone;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, BookingDetail bookingDetail);
    BookingResponse createBookingHavePayment(Booking booking, BookingDetail bookingDetail) throws UnsupportedEncodingException;
    PaymentRespone payUnpaidDepositMoney(Long id) throws UnsupportedEncodingException;
    PaymentRespone payTotalMoney(Long id) throws UnsupportedEncodingException;
    Booking getBookingById(Long id);
    public void automaticallySetCancelledBooking();
    public void automaticallySetBirdCategoryFromCancelledBooking();
    List<Booking> getAllBookings();
    List<Booking> getBookingsByUserId(Long id);
    List<Booking> getBookingByUser();
    Booking updateStatusBooking(Long bookingId, BookingStatus status) throws UnsupportedEncodingException;
    Booking updateTotalPaymentBooking(Long bookingId, float total);
    Booking updateTimeBooking(Long bookingId, String dateString);
    Booking deleteBooking(Long id);
}
