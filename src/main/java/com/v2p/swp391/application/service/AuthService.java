package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.BookingDetail;
import com.v2p.swp391.application.request.LoginRequest;
import com.v2p.swp391.application.request.SetPasswordByForgotRequest;
import com.v2p.swp391.application.request.SignUpRequest;
import com.v2p.swp391.application.response.AuthResponse;

import java.util.List;

public interface AuthService {
    AuthResponse signIn(LoginRequest request);
    void signUp(SignUpRequest request);
    void verify(Long userId,String token);
    String refresh(String accessToken);
    public void sendMailForgotPassword(String email);

    void setPassword(Long userId, String token, SetPasswordByForgotRequest request);
}
