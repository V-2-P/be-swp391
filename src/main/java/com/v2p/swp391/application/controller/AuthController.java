package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.request.LoginRequest;
import com.v2p.swp391.application.request.SignUpRequest;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.application.response.AuthResponse;
import com.v2p.swp391.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("${app.api.version.v1}/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public CoreApiResponse<AuthResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse res = authService.signIn(loginRequest);
        return CoreApiResponse.success(res);
    }

    @PostMapping("/signup")
    public CoreApiResponse<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return CoreApiResponse.success("User registered successfully");
    }

}
