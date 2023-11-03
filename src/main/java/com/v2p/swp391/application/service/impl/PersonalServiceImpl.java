package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.UserHttpMapper;
import com.v2p.swp391.application.model.RoleEntity;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.repository.RoleRepository;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.request.PersonalUpdateRequest;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.service.PersonalService;
import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.security.UserPrincipal;
import com.v2p.swp391.utils.UploadImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getPersonalInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        return user;
    }

    @Override
    public User updatePersonalInformation(PersonalUpdateRequest update) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();


        if(update.getCurrentPassword() != null && user.getPassword() != null) {
            if (passwordEncoder.matches(update.getCurrentPassword(), user.getPassword())) {
                if (update.getPassword().equals(update.getConfirmedPassword())) {
                    if (update.getPassword() != null) {
                        update.setPassword(passwordEncoder.encode(update.getPassword()));
                    }
                } else {
                    throw new AppException(HttpStatus.BAD_REQUEST, "Confirmed password is wrong!!");
                }
            } else {
                throw new AppException(HttpStatus.BAD_REQUEST, "Wrong password!!");
            }
        }
        UserHttpMapper.INSTANCE.updatePersonalFromRequest(update, user);
        return userRepository.save(user);
    }

    @Override
    public User uploadAvatar(MultipartFile imageFile) throws IOException {
        User user = getPersonalInformation();

        user.setImageUrl(UploadImageUtils.storeFile(imageFile, Image.USER_IMAGE_PATH));
        return userRepository.save(user);
    }
}
