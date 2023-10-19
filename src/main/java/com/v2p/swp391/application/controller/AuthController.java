package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.request.LoginRequest;
import com.v2p.swp391.application.request.RefreshRequest;
import com.v2p.swp391.application.request.SignUpRequest;
import com.v2p.swp391.application.response.RefreshReponse;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.application.response.AuthResponse;
import com.v2p.swp391.application.service.AuthService;
import com.v2p.swp391.config.AppProperties;
import com.v2p.swp391.exception.AppException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("${app.api.version.v1}/auth")
public class AuthController {

    private AppProperties appProperties;

    private final AuthService authService;

    @GetMapping("/verify")
    public CoreApiResponse<?> verify(
            @RequestParam Long userId,
            @RequestParam String token
    ) {
        authService.verify(userId,token);
        return CoreApiResponse.success("User verified successfully");
    }

    @PostMapping("/refresh")
    public CoreApiResponse<?> refresh(
            @CookieValue(value = "refreshToken", required = false) String cookieRT,
            @RequestBody RefreshRequest bodyRT
    ) {
        if(!isValidToken(bodyRT.getRefreshToken()) && !isValidToken(cookieRT)){
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        String token = isValidToken(bodyRT.getRefreshToken()) ? bodyRT.getRefreshToken() : cookieRT;

        String accessToken = authService.refresh(token);

        return CoreApiResponse.success(new RefreshReponse(accessToken),"User refresh token successfully");
    }

    @PostMapping("/signin")
    public CoreApiResponse<AuthResponse> signin(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        AuthResponse res = authService.signIn(loginRequest);
        Cookie cookie = new Cookie("refreshToken", res.getRefreshToken());

        // expires in 15 minutes
        cookie.setMaxAge(appProperties.getAuth().getAccessTokenExpirationMsec());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return CoreApiResponse.success(res);
    }

    @PostMapping("/signup")
    public CoreApiResponse<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return CoreApiResponse.success("User registered successfully");
    }

    private boolean isValidToken(String token) {
        return token != null && isJWT(token);
    }
    private boolean isJWT(String token) {
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }
}
