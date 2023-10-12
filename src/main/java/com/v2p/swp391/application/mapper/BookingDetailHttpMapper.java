package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.request.BookingDetailRequest;
import com.v2p.swp391.application.request.BookingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingDetailHttpMapper {
    BookingDetailHttpMapper INSTANCE = Mappers.getMapper(BookingDetailHttpMapper.class);

    @Mapping(source = "birdTypeId", target = "birdType.id")
    @Mapping(source = "fatherBirdId", target = "fatherBird.id")
    @Mapping(source = "motherBirdId", target = "motherBird.id")
    BookingDetail toModel(BookingDetailRequest bookingDetailRequest);
}
