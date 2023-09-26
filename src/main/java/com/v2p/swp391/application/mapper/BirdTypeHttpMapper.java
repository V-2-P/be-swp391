package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.BirdType;
import com.v2p.swp391.application.request.BirdTypeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface BirdTypeHttpMapper {
    BirdTypeHttpMapper INSTANCE = Mappers.getMapper(BirdTypeHttpMapper.class);
    BirdType toModel(BirdTypeRequest request);
}
