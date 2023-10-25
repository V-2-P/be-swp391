package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.UserRequest;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.service.UserService;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/role/{roleid}")
    public CoreApiResponse<List<User>> getUserByRoleID(
            @Valid @PathVariable Long roleid
    ){
        List<User> users = userService.getUserByRoleID(roleid);
        return CoreApiResponse.success(users, "Successfully!");
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

    @DeleteMapping("/{userId}")
    public CoreApiResponse<?> deleteUser(
            @PathVariable Long userId)
    {
        User deletedUser = userService.reverseStatusUser(userId);
        return CoreApiResponse.success(deletedUser, "Avatar uploaded successfully.");
    }

    @GetMapping("/me")
    public CoreApiResponse<User> getPersonalInfor(){
        User user = userService.loadPersonalInformation();
        return CoreApiResponse.success(user);
    }
}
