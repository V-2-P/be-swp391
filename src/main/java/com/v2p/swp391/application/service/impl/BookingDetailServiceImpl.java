package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.service.BookingDetailService;
import com.v2p.swp391.common.constant.BookingDetailStatus;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class BookingDetailServiceImpl implements BookingDetailService {
    private final BookingDetailRepository bookingDetailRepository;
    private final BookingRepository bookingRepository;
    private final BirdTypeRepository birdTypeRepository;
    private final BirdRepository birdRepository;

    @Override
    public BookingDetail createBookingDetail(Booking booking, BookingDetail bookingDetail) {
//        Booking booking = bookingRepository
//                        .findById(bookingDetail.getBooking().getId())
//                        .orElseThrow(()
//                                -> new ResourceNotFoundException("Booking", "id", bookingDetail.getBooking().getId())) ;

        BirdType birdType = birdTypeRepository
                        .findById(bookingDetail.getBirdType().getId())
                        .orElseThrow(()
                                -> new ResourceNotFoundException("BirdType", "id", bookingDetail.getBirdType().getId()));
        Bird fatherBird = birdRepository
                        .findById(bookingDetail.getFatherBird().getId())
                        .orElseThrow(()
                                -> new ResourceNotFoundException("Bird", "id", bookingDetail.getFatherBird().getId()));
        Bird motherBird = birdRepository
                .findById(bookingDetail.getMotherBird().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Bird", "id", bookingDetail.getMotherBird().getId()));

        bookingDetail.setBooking(booking);
        bookingDetail.setStatus(BookingDetailStatus.WAITING);
        return bookingDetailRepository.save(bookingDetail);
    }

    @Override
    public BookingDetail getBookingDetailById(Long id) {
        BookingDetail bookingDetail = bookingDetailRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("BookingDetail", "id", id));

        return bookingDetail;
    }

    @Override
    public List<BookingDetail> getBookingDetailByBookingId(Long id) {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Booking", "id", id));
        return bookingDetailRepository.findByBookingId(id);
    }

    private boolean checkValidateStatus(BookingDetail bookingDetail, String status){
        List<String> statuses = new ArrayList<String>();
        statuses.add(BookingDetailStatus.WAITING);
        statuses.add(BookingDetailStatus.IN_BREEDING_PROGRESS);
        statuses.add(BookingDetailStatus.BROODING);
        statuses.add(BookingDetailStatus.FLEDGLING);
        statuses.add(BookingDetailStatus.FAILED);

        int bookingDetailStatusIndex = -1;
        int statusIndex = -1;
        for (int i = 0; i < statuses.size(); i++){
            if(statuses.get(i).equalsIgnoreCase(status)) statusIndex = i;
            if(statuses.get(i).equalsIgnoreCase(bookingDetail.getStatus())) bookingDetailStatusIndex = i;
        }

        if(statusIndex == -1 || statusIndex < bookingDetailStatusIndex)
            return false;
        return true;
    }

    @Override
    public BookingDetail updateBookingDetailStatus(Long id, String status) {
        BookingDetail existingBookingDetail = this.getBookingDetailById(id);
        if(!checkValidateStatus(existingBookingDetail, status))
            throw new AppException(HttpStatus.BAD_REQUEST, "Status is wrong format");
        
        existingBookingDetail.setStatus(status);
        return bookingDetailRepository.save(existingBookingDetail);
    }

    @Override
    public BookingDetail deleteBookingDetail(Long id) {
        BookingDetail bookingDetail = this.getBookingDetailById(id);
        bookingDetailRepository.deleteById(id);
        return bookingDetail;
    }
}
