package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.service.BookingDetailService;
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
    private final CategoryRepository categoryRepository;
    private final BirdPairingRepository birdPairingRepository;
    private final BirdParingServiceImpl birdParingService;

    @Override
    public BookingDetail createBookingDetail(Booking booking, BookingDetail bookingDetail) {
        BirdType birdType = birdTypeRepository
                        .findById(bookingDetail.getBirdType().getId())
                        .orElseThrow(()
                                -> new ResourceNotFoundException("BirdType", "id", bookingDetail.getBirdType().getId()));

        bookingDetail.setBooking(booking);
        bookingDetail.setStatus(BookingDetailStatus.Waiting);
        return bookingDetailRepository.save(bookingDetail);
    }

    @Override
    public BookingDetail getBookingDetailById(Long id) {
        BookingDetail bookingDetail = bookingDetailRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("BookingDetail", "id", id));

        return bookingDetail;
    }


    private boolean checkValidateStatus(BookingDetail bookingDetail, BookingDetailStatus status){
        List<Object> statuses = new ArrayList<Object>();
        statuses.add(BookingDetailStatus.Waiting);
        statuses.add(BookingDetailStatus.In_Breeding_Progress);
        statuses.add(BookingDetailStatus.Brooding);
        statuses.add(BookingDetailStatus.Fledgling_All);
        statuses.add(BookingDetailStatus.Failed);

        int bookingDetailStatusIndex = -1;
        int statusIndex = -1;
        for (int i = 0; i < statuses.size(); i++){
            if(statuses.get(i).equals(status)) statusIndex = i;
            if(statuses.get(i).equals(bookingDetail.getStatus())) bookingDetailStatusIndex = i;
        }

        if(statusIndex == -1 || statusIndex < bookingDetailStatusIndex)
            return false;
        return true;
    }

    @Override
    public BookingDetail updateBookingDetailStatus(Long id, BookingDetailStatus status) {
        BookingDetail existingBookingDetail = this.getBookingDetailById(id);
        if(!checkValidateStatus(existingBookingDetail, status))
            throw new AppException(HttpStatus.BAD_REQUEST, "Status is wrong format");
//        if(status.equals(BookingDetailStatus.Brooding)){
//            BirdPairing newBirdPairing = new BirdPairing();
//            newBirdPairing.setBookingDetail(existingBookingDetail);
//            existingBookingDetail.setStatus(status);
//
//            birdParingService.createBirdPairing(newBirdPairing);
//        }
        existingBookingDetail.setStatus(status);
        return bookingDetailRepository.save(existingBookingDetail);
    }

    @Override
    public BookingDetail deleteBookingDetail(Long id) {
        BookingDetail bookingDetail = this.getBookingDetailById(id);
        bookingDetail.getBooking().setStatus(BookingStatus.Cancelled);
        bookingDetailRepository.deleteById(id);
        return bookingDetail;
    }
}
