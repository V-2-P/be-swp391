package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.PersonalUpdateRequest;
import com.v2p.swp391.application.response.UserResponse;
import com.v2p.swp391.application.service.impl.PersonalServiceImpl;
import com.v2p.swp391.security.CurrentUser;
import com.v2p.swp391.security.UserPrincipal;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.application.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.v2p.swp391.application.mapper.UserHttpMapper.INSTANCE;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("${app.api.version.v1}/user")
@PreAuthorize("isAuthenticated() && hasAnyRole('CUSTOMER', 'ADMIN', 'MANAGER', 'STAFF')")
public class PersonalController {

    private final PersonalServiceImpl personalService;

    @GetMapping("/me")
    public CoreApiResponse<User> getCurrentUser() {
        return CoreApiResponse.success(personalService.getPersonalInformation());
    }

    @PutMapping("")
    public CoreApiResponse<User> updateUser(
            @RequestBody PersonalUpdateRequest personalUpdateRequest
    ) {
        User user = personalService.updatePersonalInformation(personalUpdateRequest);
        return CoreApiResponse.success(user);
    }

    @PostMapping("/uploadavatar")
    public CoreApiResponse<?> uploadThumbnail(
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException
    {
        personalService.uploadAvatar(imageFile);
        return CoreApiResponse.success("Avatar uploaded successfully.");
    }
}
