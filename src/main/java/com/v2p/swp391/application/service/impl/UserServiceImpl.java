package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.UserHttpMapper;
import com.v2p.swp391.application.model.RoleEntity;
import com.v2p.swp391.application.repository.RoleRepository;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.service.UserService;
import com.v2p.swp391.security.UserPrincipal;
import com.v2p.swp391.utils.UploadImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

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

    @Override
    public List<User> getUserByRoleID(Long id) {
        RoleEntity role = roleRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Role", "id", id));

        List<User> users = userRepository.getUsersByRoleEntityId(id);
        return users;
    }

    @Override
    public User updateUser(Long id, UserUpdateRequest update) {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));

        UserHttpMapper.INSTANCE.updateUserFromRequest(update, existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public User uploadAvatar(Long id, MultipartFile imageFile) throws IOException {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));

        existingUser.setImageUrl(UploadImageUtils.storeFile(imageFile));
        return userRepository.save(existingUser);
    }

    @Override
    public User reverseStatusUser(Long id) {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));

        if(existingUser.getIsActive() == 0)
            existingUser.setIsActive(1);
        else
            existingUser.setIsActive(0);

        return userRepository.save(existingUser);
    }

    @Override
    public User loadPersonalInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        return user;
    }


}
