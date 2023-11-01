package com.v2p.swp391.application.mapper;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.CategoryRequest;
import com.v2p.swp391.application.request.PersonalUpdateRequest;
import com.v2p.swp391.application.request.UserRequest;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.response.UserResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserHttpMapper {
    UserHttpMapper INSTANCE = Mappers.getMapper(UserHttpMapper.class);
    @Mapping(source = "roleId", target = "roleEntity.id")
    User toModel(UserRequest request);
    @Mapping(source = "roleEntity.name", target = "roleEntity")
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "isActive", target = "isActive")
    public void updateUserFromRequest(UserUpdateRequest updateRequest, @MappingTarget User user);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updatePersonalFromRequest(PersonalUpdateRequest updateRequest, @MappingTarget User user);
}
