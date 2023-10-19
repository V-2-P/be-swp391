package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.BirdPairing;
import com.v2p.swp391.application.request.BirdParingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface BirdPairingHttpMapper {
    BirdPairingHttpMapper INSTANCE = Mappers.getMapper(BirdPairingHttpMapper.class);

    @Mapping(source = "bookingDetailId", target = "bookingDetail.id")
    BirdPairing toModel(BirdParingRequest birdParingRequest);
}
