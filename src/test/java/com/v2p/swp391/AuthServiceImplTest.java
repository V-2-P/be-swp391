package com.v2p.swp391;

import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.request.LoginRequest;
import com.v2p.swp391.application.request.SignUpRequest;
import com.v2p.swp391.application.response.AuthResponse;
import com.v2p.swp391.application.service.impl.AuthServiceImpl;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl")
public class AuthServiceImplTest {


    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;


    @Test
    public void signIn_Successful() {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(tokenProvider.createToken(authentication)).thenReturn("fake_token");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        AuthResponse authResponse = authService.signIn(loginRequest);

        assertEquals("fake_token", authResponse.getAccessToken());
    }

    @Test
    public void signIn_EmailDoesNotExist() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found with email : test@example.com"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.signIn(loginRequest);
        });

        assertEquals("User not found with email : test@example.com", exception.getMessage());
    }

    @Test
    public void signUp_Successful() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("Tester");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");

        authService.signUp(signUpRequest);

        verify(userRepository).save(argThat(user -> {
            return "Tester".equals(user.getName()) &&
                    "test@example.com".equals(user.getEmail()) &&
                    "hashed_password".equals(user.getPassword()) &&
                    !user.getEmailVerified();
        }));
    }

    @Test
    public void signUp_EmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@example.com");

        AppException exception = assertThrows(AppException.class, () -> {
            authService.signUp(signUpRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getCode());
        assertEquals("Email address already in use.", exception.getMessage());
    }
}
