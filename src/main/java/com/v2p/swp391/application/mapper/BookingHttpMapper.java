package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.request.BookingDetailRequest;
import com.v2p.swp391.application.request.BookingRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingHttpMapper {
    BookingHttpMapper INSTANCE = Mappers.getMapper(BookingHttpMapper.class);
    @Mapping(source = "userId", target = "user.id")
    Booking toModel(BookingRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookingInfoFromRequest(BookingRequest request, @MappingTarget Booking booking);
}
