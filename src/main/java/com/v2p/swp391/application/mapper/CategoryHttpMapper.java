package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface CategoryHttpMapper {
    CategoryHttpMapper INSTANCE = Mappers.getMapper(CategoryHttpMapper.class);
    Category toModel(CategoryRequest request);
}
