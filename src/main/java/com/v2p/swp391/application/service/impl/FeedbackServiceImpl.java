package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.*;
import com.v2p.swp391.application.repository.BirdRepository;
import com.v2p.swp391.application.repository.FeedBackRepository;
import com.v2p.swp391.application.repository.FeedbackBirdRepository;
import com.v2p.swp391.application.repository.OrderRepository;
import com.v2p.swp391.application.service.FeedbackService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final OrderRepository orderRepository;
    private final FeedbackBirdRepository feedbackBirdRepository;
    private final BirdRepository birdRepository;

    @Override
    public Feedback createFeedback(Feedback feedback, List<FeedbackBird> feedbackBirds) {
        Order order = orderRepository.findById(feedback.getOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order","id",feedback.getOrder().getId()));

        if (feedBackRepository.existsByOrderId(feedback.getOrder().getId())) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Feedback for this order already exists.");
        }

        feedback.setOrder(order);


        for (FeedbackBird feedbackBird : feedbackBirds) {
            Long birdId = feedbackBird.getBird().getId();
            Bird bird = birdRepository.findById(birdId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bird", "id", birdId));
            feedBackRepository.save(feedback);
            feedbackBird.setBird(bird);
            feedbackBird.setFeedback(feedback);
            feedbackBirdRepository.save(feedbackBird);
        }
        feedback.setFeedbackBirds(feedbackBirds);
        return feedback;
    }

    @Override
    public List<Feedback> getFeedbackByOrderID(Long id) {
        return feedBackRepository.findByOrderId(id);
    }

    @Override
    public List<FeedbackBird> getFeedbackByBirdId(Long id) {
        return feedbackBirdRepository.findByBirdId(id);

    }
}
