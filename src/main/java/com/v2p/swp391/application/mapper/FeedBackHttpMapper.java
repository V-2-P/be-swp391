package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.Feedback;
import com.v2p.swp391.application.model.FeedbackBird;
import com.v2p.swp391.application.request.FeedbackBirdRequest;
import com.v2p.swp391.application.request.FeedbackRequest;
import com.v2p.swp391.application.response.FeedBackBirdResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface FeedBackHttpMapper {
    FeedBackHttpMapper INSTANCE = Mappers.getMapper(FeedBackHttpMapper.class);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "birdFeedbacks", target = "feedbackBirds")
    Feedback toModel(FeedbackRequest request);

    @Mapping(source = "birdId", target = "bird.id")
    List<FeedbackBird> toListFeedBack(List<FeedbackBirdRequest>birdFeedbackRequests);
    @Mapping(source = "birdId", target = "bird.id")
    FeedbackBird toFeedbackBird(FeedbackBirdRequest request);


    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "feedbackBirds", target = "birdFeedbacks")
    FeedbackRequest toRequest(Feedback feedback);

    @Mapping(source = "bird.id", target = "birdId")
    FeedbackBirdRequest toFeedbackBirdRequest(FeedbackBird request);

    List<FeedbackRequest> toListRequest(List<Feedback> feedbacks);

    List<FeedbackBirdRequest> toListFeedbackBirdRequest(List<FeedbackBird> request);

    @Mapping(source = "bird.id", target = "birdId")
    @Mapping(source = "bird.name", target = "birdName")
    @Mapping(source = "feedback.order.id", target = "orderId")
    @Mapping(source = "bird.birdType.name", target = "birdType")
    @Mapping(source = "feedback.order.user.fullName", target = "fullName")
    @Mapping(source = "feedback.order.user.imageUrl", target = "userImage")
    @Mapping(source = "feedback.order.user.address", target = "address")
    @Mapping(source = "feedback.order.user.phoneNumber", target = "phoneNumber")
    FeedBackBirdResponse toResponse(FeedbackBird feedbackBird);
    @Mapping(source = "bird.id", target = "birdId")
    @Mapping(source = "feedback.order.id", target = "orderId")
    @Mapping(source = "bird.name", target = "birdName")
    @Mapping(source = "feedback.order.user.fullName", target = "fullName")
    @Mapping(source = "feedback.order.user.address", target = "address")
    @Mapping(source = "feedback.order.user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "bird.birdType.name", target = "birdType")
    List<FeedBackBirdResponse> toListResponse(List<FeedbackBird> feedbackBird);

}
