package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.FeedbackBooking;
import com.v2p.swp391.application.request.FeedbackBookingRequest;
import com.v2p.swp391.application.request.FeedbackBookingUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FeedbackBookingHttpMapper {
    FeedbackBookingHttpMapper INSTANCE = Mappers.getMapper(FeedbackBookingHttpMapper.class);

    @Mapping(source = "bookingId", target = "booking.id")
    FeedbackBooking toModel(FeedbackBookingRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "rating", target = "rating")
    public void updateFeedbackBookingFromRequest(FeedbackBookingUpdateRequest request, @MappingTarget FeedbackBooking feedbackBooking);
}
