package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.Bird;

import com.v2p.swp391.application.request.BirdRequest;
import com.v2p.swp391.application.response.BirdDetailResponse;
import com.v2p.swp391.application.response.BirdResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")

public interface BirdHttpMapper {
    BirdHttpMapper INSTANCE = Mappers.getMapper(BirdHttpMapper.class);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "typeId", target = "birdType.id")
    Bird toModel(BirdRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "birdType.name", target = "birdType")
    BirdResponse toResponse(Bird bird);

    List<BirdResponse> toListResponses(List<Bird> birds);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBirdFromRequest(BirdRequest request, @MappingTarget Bird bird);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "birdType.name", target = "birdType")
    @Mapping(source = "description", target = "description")
    BirdDetailResponse toDetail(Bird bird);


}
