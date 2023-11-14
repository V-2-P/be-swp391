package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.UserRequest;
import com.v2p.swp391.application.request.UserUpdateRequest;
import com.v2p.swp391.application.response.UserPageRespone;
import com.v2p.swp391.application.response.UserResponse;
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
    public CoreApiResponse<UserResponse> getUserById(
            @PathVariable Long userId
    ){
        UserResponse user = userService.getUserResponeById(userId);
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



    @GetMapping("")
    public CoreApiResponse<UserPageRespone> getUsers(
            @RequestParam(defaultValue = "0", name = "roleId") Long roleId,
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );

        List<UserResponse> userPage = userService.getAllUser(roleId, keyword);
        int totalPages = (userPage.size() % limit == 0) ? userPage.size() / limit : userPage.size() / limit + 1;
        UserPageRespone userPageRespone = new UserPageRespone();
        userPageRespone.setUsers(userPage);
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
