package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.model.Feedback;
import com.v2p.swp391.application.model.FeedbackBird;


import java.util.List;

public interface FeedbackOrderService {
    Feedback createFeedback(Feedback voucher, List<FeedbackBird> feedbackBirds);

    List<Feedback> getFeedbackByOrderID(Long id);

    List<FeedbackBird> getAll();

    FeedbackBird getFeedbackBirdById(Long id);

    List<FeedbackBird> getFeedbackByBirdId(Long id);

    List<FeedbackBird> getFeedbackBirdsByBirdType(Long birdType);

    void deleteFeedback(Long id);


}
