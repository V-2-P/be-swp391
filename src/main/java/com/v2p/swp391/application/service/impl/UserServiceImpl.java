package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    }

    @Override
    public void create(User user) {
        user.setEmailVerified(true);
//        user.setProvider(Image.DEFAULT_AUTH_PROVIDER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageUrl(Image.USER_IMAGE_PATH+Image.DEFAULT_AVATAR);

        userRepository.save(user);
    }
}
