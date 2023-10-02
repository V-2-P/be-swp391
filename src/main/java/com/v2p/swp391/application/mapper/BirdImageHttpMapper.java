package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.BirdImage;
import com.v2p.swp391.application.request.BirdImageRequest;
import com.v2p.swp391.application.response.BirdImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BirdImageHttpMapper {
    BirdImageHttpMapper INSTANCE = Mappers.getMapper(BirdImageHttpMapper.class);

    BirdImage toModel(BirdImageRequest request);


    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "id", target = "id")
    List<BirdImageResponse> toListResponse(List<BirdImage> image);

    BirdImageResponse toResponse(BirdImage birdImage);
}
