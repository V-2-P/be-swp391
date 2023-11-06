package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User findById(Long id);
    void create(User user);
    User getUserById(Long id);
    UserResponse getUserResponeById(Long id);
    User updateUser(Long id, UserUpdateRequest update);
    List<UserResponse> getAllUser(Long roleId, String keyword, PageRequest pageRequest);
    User deleteUser(Long id);
}
