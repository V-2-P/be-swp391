package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.User;

public interface UserService {
    User findById(Long id);
    void create(User user);
}
