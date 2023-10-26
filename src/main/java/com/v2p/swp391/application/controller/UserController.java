package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.UserRequest;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.response.UserPageRespone;
import com.v2p.swp391.application.service.UserService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.v2p.swp391.application.mapper.UserHttpMapper.INSTANCE;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("${app.api.version.v1}/users")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public CoreApiResponse<?> createUser(@Valid @RequestBody UserRequest request){
        userService.create(INSTANCE.toModel(request));
        return CoreApiResponse.success("User was created");
    }

    @GetMapping("/{userId}")
    public CoreApiResponse<User> getUserById(
            @PathVariable Long userId
    ){
        User user = userService.getUserById(userId);
        return CoreApiResponse.success(user);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public String userAccess() {
        return "User Content.";
    }
    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerAccess() {

        return "Customer Content.";
    }

    @PutMapping("/{id}")
    public CoreApiResponse<User> updateUser(
            @Valid @PathVariable Long id,
            @RequestBody UserUpdateRequest updateRequest
    ){
        User user = userService.updateUser(id, updateRequest);
        return CoreApiResponse.success(user, "Successfully!");
    }

    @PostMapping("/uploadavatar/{userId}")
    public CoreApiResponse<?> uploadThumbnail(
            @PathVariable Long userId,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException
    {
        userService.uploadAvatar(userId, imageFile);
        return CoreApiResponse.success("Thumbnail uploaded successfully.");
    }

    @GetMapping("")
    public CoreApiResponse<UserPageRespone> getUsers(
            @RequestParam(defaultValue = "0", name = "roleId") Long roleId,
            @RequestParam(defaultValue = "", name = "email") String email,
            @RequestParam(defaultValue = "", name = "phoneNumber") String phoneNumber,
            @RequestParam(defaultValue = "", name = "fullName") String fullName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );

        Page<User> userPage = userService.getAllUser(roleId, fullName, phoneNumber, email, pageRequest);
        int totalPages = userPage.getTotalPages();
        List<User> users = userPage.getContent();
        UserPageRespone userPageRespone = new UserPageRespone();
        userPageRespone.setUsers(users);
        userPageRespone.setTotalPages(totalPages);
        return CoreApiResponse.success(userPageRespone);
    }

    @DeleteMapping("/{userId}")
    public CoreApiResponse<?> deleteUser(
            @PathVariable Long userId)
    {
        User deletedUser = userService.deleteUser(userId);
        return CoreApiResponse.success(deletedUser, "Avatar uploaded successfully.");
    }

}
