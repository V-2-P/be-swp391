package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.model.Feedback;
import com.v2p.swp391.application.model.FeedbackBird;
import com.v2p.swp391.application.request.FeedbackBirdRequest;
import com.v2p.swp391.application.request.FeedbackRequest;
import com.v2p.swp391.application.response.FeedBackBirdResponse;
import com.v2p.swp391.application.service.FeedbackOrderService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.FeedBackHttpMapper.INSTANCE;


@RestController
@RequestMapping("${app.api.version.v1}/feedbackbirds")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackOrderService feedbackService;

    @PostMapping("")
    public CoreApiResponse<Feedback> createFeedback(
            @Valid @RequestBody FeedbackRequest request) {
        Feedback feedback = feedbackService.createFeedback(INSTANCE.toModel(request)
                , INSTANCE.toListFeedBack(request.getBirdFeedbacks()));
        return CoreApiResponse.success(feedback, "Create feedback successfully");
    }

    @GetMapping("/order/{order_id}")
    public CoreApiResponse<List<Feedback>> getFeedbackByOrderId(
            @Valid @PathVariable("order_id") Long orderId) {
        List<Feedback> feedbacks = feedbackService.getFeedbackByOrderID(orderId);
        return CoreApiResponse.success(feedbacks);
    }

    @GetMapping("/bird/{bird_id}")
    public CoreApiResponse<List<FeedBackBirdResponse>> getFeedbackByBirdId(
            @Valid @PathVariable("bird_id") Long birdId) {
        List<FeedbackBird> feedbackBirds = feedbackService.getFeedbackByBirdId(birdId);

        return CoreApiResponse.success(INSTANCE.toListResponse(feedbackBirds));
    }

    @GetMapping("/{id}")
    public CoreApiResponse<FeedBackBirdResponse> getFeedbackBirdById(@Valid @PathVariable Long id){
        FeedbackBird feedbackBird = feedbackService.getFeedbackBirdById(id);
        return CoreApiResponse.success(INSTANCE.toResponse(feedbackBird));
    }

    @GetMapping()
    public CoreApiResponse<List<FeedBackBirdResponse>> getAll() {
        List<FeedbackBird> feedbackBirds = feedbackService.getAll();
        return CoreApiResponse.success(INSTANCE.toListResponse(feedbackBirds));
    }

    @GetMapping("/birdtype/{bird_type}")
    public CoreApiResponse<List<FeedBackBirdResponse>> getFeedbackByBirdType(
            @Valid @PathVariable("bird_type") Long birdId) {
        List<FeedbackBird> feedbackBirds = feedbackService.getFeedbackBirdsByBirdType(birdId);
        return CoreApiResponse.success(INSTANCE.toListResponse(feedbackBirds));
    }

    @PutMapping("/hidden/{id}")
    public CoreApiResponse<?> hiddenFeedback(@Valid @PathVariable("id") Long id ){
        feedbackService.deleteFeedback(id);
        return CoreApiResponse.success("Hidden feedback successfully");
    }
}
