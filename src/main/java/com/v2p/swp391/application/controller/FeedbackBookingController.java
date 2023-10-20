package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.mapper.FeedbackBookingHttpMapper;
import com.v2p.swp391.application.model.FeedbackBooking;
import com.v2p.swp391.application.request.FeedbackBookingRequest;
import com.v2p.swp391.application.request.FeedbackBookingUpdateRequest;
import com.v2p.swp391.application.service.FeedbackBookingService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/feedbackbooking")
@RequiredArgsConstructor
public class FeedbackBookingController {
    public final FeedbackBookingService feedbackBookingService;

    @PostMapping("")
    public CoreApiResponse<FeedbackBooking> createFeedbackBooking(
            @Valid @RequestBody FeedbackBookingRequest request
    ){
        FeedbackBooking newFeedbackBooking = feedbackBookingService
                .createFeedbackBooking(FeedbackBookingHttpMapper.INSTANCE.toModel(request));
        return CoreApiResponse.success(newFeedbackBooking, "Successfully!!");
    }

    @GetMapping("")
    public CoreApiResponse<List<FeedbackBooking>> getAllFeedbackBooking(){
        List<FeedbackBooking> feedbackBookings = feedbackBookingService.getAllFeedbackBookings();
        return CoreApiResponse.success(feedbackBookings, "Successfully!!");
    }

    @GetMapping("/{id}")
    public CoreApiResponse<FeedbackBooking> getFeedbackBookingById(
            @Valid @PathVariable Long id
    ){
        FeedbackBooking feedbackBooking = feedbackBookingService.getFeedbackBookingById(id);
        return CoreApiResponse.success(feedbackBooking, "Successfully!!");
    }

    @PutMapping("/{id}")
    public CoreApiResponse<FeedbackBooking> updateFeedbackBooking(
            @Valid @PathVariable Long id,
            @RequestBody FeedbackBookingUpdateRequest request
    ){
        FeedbackBooking feedbackBooking = feedbackBookingService.updateFeedbackBooking(id, request);
        return CoreApiResponse.success(feedbackBooking, "Successfully!!");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<FeedbackBooking> reverseStatusFeedbackBooking(
            @Valid @PathVariable Long id
    ){
        FeedbackBooking feedbackBooking = feedbackBookingService.deleteFeedbackBooking(id);
        return CoreApiResponse.success(feedbackBooking, "Successfully!!");
    }
}
