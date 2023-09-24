package com.v2p.swp391.application.service;

import com.v2p.swp391.application.request.LoginRequest;
import com.v2p.swp391.application.request.SignUpRequest;
import com.v2p.swp391.application.response.AuthResponse;

public interface AuthService {
    AuthResponse signIn(LoginRequest request);
    void signUp(SignUpRequest request);
}
