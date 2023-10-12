package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Feedback;
import com.v2p.swp391.application.model.FeedbackBird;
import com.v2p.swp391.application.request.CartItemRequest;
import com.v2p.swp391.application.request.FeedbackRequest;


import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(Feedback voucher, List<FeedbackBird> feedbackBirds);
    List<Feedback> getFeedbackByOrderID(Long id);
    List<FeedbackBird> getFeedbackByBirdId(Long id);

}
