package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.BookingHttpMapper;
import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.BookingRepository;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.service.BookingDetailService;
import com.v2p.swp391.application.service.BookingService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class    BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingDetailService bookingDetailService;
    private final BookingHttpMapper bookingMapper;

    @Override
    public Booking createBooking(Booking booking, BookingDetail bookingDetail) {
        User user = userRepository
                        .findById(booking.getUser().getId())
                        .orElseThrow(()-> new ResourceNotFoundException("User", "id", booking.getUser().getId()));
        Booking createdBooking = bookingRepository.save(booking);
        bookingDetailService.createBookingDetail(createdBooking, bookingDetail);

        booking.setStatus(BookingStatus.Pending);
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

    private boolean checkFormatStatus(Booking booking, BookingStatus status){
        List<Object> statuses = new ArrayList<Object>();
        statuses.add(BookingStatus.Pending);
        statuses.add(BookingStatus.Confirmed);
        statuses.add(BookingStatus.Completed);
        statuses.add(BookingStatus.Cancelled);

        int bookingStatusIndex = -1;
        int statusIndex = 1;
        for (int i = 0; i < statuses.size(); i++){
            if(statuses.get(i).equals(status)) statusIndex = i;
            if(statuses.get(i).equals(booking.getStatus())) bookingStatusIndex = i;
        }

        //Status should follow flow: Pending -> Confirmed -> Completed -> Cancelled
        //Correct status format
        if(statusIndex == -1 || statusIndex < bookingStatusIndex)
            return false;
        return true;
    }
    @Override
    public Booking updateStatusBooking(Long bookingId, BookingStatus status) {
        Booking existingBooking = getBookingById(bookingId);
        if(!checkFormatStatus(existingBooking, status))
            throw new AppException(HttpStatus.BAD_REQUEST, "Status is wrong format");
        if(status.equals(BookingStatus.Confirmed))
            existingBooking.getBookingDetail().setStatus(BookingDetailStatus.In_Breeding_Progress);
        existingBooking.setStatus(status);
        return bookingRepository.save(existingBooking);
    }

    @Override
    public Booking updateTotalPaymentBooking(Long bookingId, Float total) {
        Booking existingBooking = getBookingById(bookingId);
        existingBooking.setTotalPayment(total);
        existingBooking.setPaymentDeposit((float) (total * 0.3));
        return bookingRepository.save(existingBooking);
    }

    @Override
    public Booking updateTimeBooking(Long bookingId, String dateString) {
        Booking existingBooking = getBookingById(bookingId);
        Date parsedTimestamp = DateUtils.parseTimestamp(dateString);
        existingBooking.setBookingTime(parsedTimestamp);
        return bookingRepository.save(existingBooking);
    }


    @Override
    public Booking deleteBooking(Long id) {
        Booking existingBooking = getBookingById(id);
        if(existingBooking.getBookingDetail() != null)
            bookingDetailService.deleteBookingDetail(existingBooking.getBookingDetail().getId());
        bookingRepository.deleteById(id);
        return existingBooking;
    }
}
