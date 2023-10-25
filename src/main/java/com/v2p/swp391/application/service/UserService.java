package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User findById(Long id);
    void create(User user);
    List<User> getUserByRoleID(Long id);
    User updateUser(Long id, UserUpdateRequest update);
    User uploadAvatar(Long id, MultipartFile imageFile) throws IOException;
    User reverseStatusUser(Long id);
    User loadPersonalInformation();
}
