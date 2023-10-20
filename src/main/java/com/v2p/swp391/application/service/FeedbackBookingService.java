package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.FeedbackBooking;
import com.v2p.swp391.application.request.FeedbackBookingUpdateRequest;

import java.util.List;

public interface FeedbackBookingService {
    public FeedbackBooking createFeedbackBooking(FeedbackBooking feedbackBooking);
    public List<FeedbackBooking> getAllFeedbackBookings();
    public FeedbackBooking getFeedbackBookingById(Long id);
    public FeedbackBooking updateFeedbackBooking(Long id, FeedbackBookingUpdateRequest request);
    public FeedbackBooking deleteFeedbackBooking(Long id);

}
