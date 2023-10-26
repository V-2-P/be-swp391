package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.UserHttpMapper;
import com.v2p.swp391.application.model.RoleEntity;
import com.v2p.swp391.application.repository.RoleRepository;
import com.v2p.swp391.application.repository.TokenRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final TokenRepository tokenRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    }

    @Override
    public void create(User user) {
        RoleEntity role = roleRepository
                .findById(user.getRoleEntity().getId())
                .orElseThrow(()
                        -> new ResourceNotFoundException("RoleEntity", "id", user.getRoleEntity().getId()));

        user.setEmailVerified(true);
//        user.setProvider(Image.DEFAULT_AUTH_PROVIDER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageUrl(Image.DEFAULT_AVATAR);
        user.setIsActive(1);

        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public User updateUser(Long id, UserUpdateRequest update) {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));

        UserHttpMapper.INSTANCE.updateUserFromRequest(update, existingUser);

        if(update.getRoleId() != null && Long.parseLong(update.getRoleId()) != existingUser.getRoleEntity().getId()){
            RoleEntity role = roleRepository
                    .findById(Long.parseLong(update.getRoleId()))
                    .orElseThrow(()
                            -> new ResourceNotFoundException("Role", "id", Long.parseLong(update.getRoleId())));

            existingUser.setRoleEntity(role);
        }

        if(update.getPassword() != null){
            update.setPassword(passwordEncoder.encode(update.getPassword()));
        }

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
    public Page<User> getAllUser(Long roleId, String fullName, String phoneNumber, String email, PageRequest pageRequest) {
        Page<User> usersPage;
        usersPage = userRepository.searchUsers(fullName, roleId, phoneNumber, email, pageRequest);
        return usersPage;
    }


    @Override
    public User deleteUser(Long id) {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));

        userRepository.deleteById(id);
        return existingUser;
    }

}
