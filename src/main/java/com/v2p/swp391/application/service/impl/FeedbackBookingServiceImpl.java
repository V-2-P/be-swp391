package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.FeedbackBookingHttpMapper;
import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.BookingDetailStatus;
import com.v2p.swp391.application.model.BookingStatus;
import com.v2p.swp391.application.model.FeedbackBooking;
import com.v2p.swp391.application.repository.BookingRepository;
import com.v2p.swp391.application.repository.FeedbackBookingRepository;
import com.v2p.swp391.application.request.FeedbackBookingUpdateRequest;
import com.v2p.swp391.application.service.FeedbackBookingService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackBookingServiceImpl implements FeedbackBookingService {
    private final BookingRepository bookingRepository;
    private final FeedbackBookingRepository feedbackBookingRepository;
    @Override
    public FeedbackBooking createFeedbackBooking(FeedbackBooking feedbackBooking) {
        Booking booking = bookingRepository
                .findById(feedbackBooking.getBooking().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("Booking", "id", feedbackBooking.getBooking().getId()));
        feedbackBooking.setBooking(booking);
        feedbackBooking.setIsActive(1);
        if(feedbackBooking.getBooking().getStatus().equals(BookingStatus.Delivered)
                || feedbackBooking.getBooking().getBookingDetail().getStatus().equals(BookingDetailStatus.Failed))
            return feedbackBookingRepository.save(feedbackBooking);
        else
            throw new AppException(HttpStatus.BAD_REQUEST, "Feedback is created when Booking status is DELIVERED or FAILED!");
    }

    @Override
    public List<FeedbackBooking> getAllFeedbackBookings() {
        return feedbackBookingRepository.findAll();
    }

    @Override
    public FeedbackBooking getFeedbackBookingById(Long id) {
        return feedbackBookingRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback Booking", "id", id));
    }

    @Override
    public FeedbackBooking updateFeedbackBooking(Long id, FeedbackBookingUpdateRequest request) {
        FeedbackBooking existingFeedbackBooking = feedbackBookingRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Feedback Booking", "id", id));

        FeedbackBookingHttpMapper.INSTANCE.updateFeedbackBookingFromRequest(request, existingFeedbackBooking);
        return feedbackBookingRepository.save(existingFeedbackBooking);
    }

    @Override
    public FeedbackBooking deleteFeedbackBooking(Long id) {
        FeedbackBooking existingFeedbackBooking = feedbackBookingRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Feedback Booking", "id", id));
        if(existingFeedbackBooking.getIsActive() == 0)
            existingFeedbackBooking.setIsActive(1);
        else
            existingFeedbackBooking.setIsActive(0);
        return feedbackBookingRepository.save(existingFeedbackBooking);
    }
}
