package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.CategoryRequest;
import com.v2p.swp391.application.request.UserRequest;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.response.UserResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserHttpMapper {
    UserHttpMapper INSTANCE = Mappers.getMapper(UserHttpMapper.class);
    User toModel(UserRequest request);
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "fullname", target = "fullName")
    public void updateUserFromRequest(UserUpdateRequest updateRequest, @MappingTarget User user);
}
