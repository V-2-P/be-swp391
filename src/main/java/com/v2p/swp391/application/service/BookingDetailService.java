package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.BookingDetail;

import java.util.List;

public interface BookingDetailService {
    BookingDetail createBookingDetail(Booking booking, BookingDetail bookingDetail);
    BookingDetail getBookingDetailById(Long id);
    List<BookingDetail> getBookingDetailByBookingId(Long id);
    BookingDetail updateBookingDetailStatus(Long id, String status);
    BookingDetail deleteBookingDetail(Long id);
}
