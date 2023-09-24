package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.response.UserResponse;
import com.v2p.swp391.security.CurrentUser;
import com.v2p.swp391.security.UserPrincipal;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.application.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.v2p.swp391.application.mapper.UserHttpMapper.INSTANCE;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("${app.api.version.v1}/user")
@PreAuthorize("isAuthenticated()")
public class PersonalController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public CoreApiResponse<UserResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return CoreApiResponse.success(INSTANCE.toResponse(userService.findById(userPrincipal.getId())));
    }
}
