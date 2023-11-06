package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.mapper.UserHttpMapper;
import com.v2p.swp391.application.model.Booking;
import com.v2p.swp391.application.model.Order;
import com.v2p.swp391.application.model.RoleEntity;
import com.v2p.swp391.application.repository.*;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.response.UserResponse;
import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.application.model.User;
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
import java.util.ArrayList;
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
    private final BookingRepository bookingRepository;
    private final OrderRepository orderRepository;

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
        User user =  userRepository.findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", id));
        return user;
    }

    @Override
    public UserResponse getUserResponeById(Long id) {
        User user = getUserById(id);
        List<Booking> bookingList = bookingRepository.findByUserId(user.getId());
        List<Order> orderList = orderRepository.findByUserId(user.getId());

        float total = 0;
        for(Booking booking: bookingList){
            if(booking.getTotalPayment() != null){
                total += booking.getTotalPayment();
            }

        }
        for(Order order: orderList){
            if(order.getPaymentMethod() != null){
                total += order.getTotalPayment();
            }
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setUser(user);
        userResponse.setBookingQuantity(bookingList.size());
        userResponse.setOrderQuantity(orderList.size());
        userResponse.setTotalMoney(total);

        return userResponse;
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
    public List<UserResponse> getAllUser(Long roleId, String keyword, PageRequest pageRequest) {
        List<User> users;
        users = userRepository.searchUsers(roleId, keyword, pageRequest);

        List<UserResponse> usersList = new ArrayList<>();
        for(User user: users){
            UserResponse userResponse = getUserResponeById(user.getId());
            usersList.add(userResponse);
        }
        return usersList;
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
