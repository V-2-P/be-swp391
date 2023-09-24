package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.request.UserRequest;
import com.v2p.swp391.application.service.UserService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
}
