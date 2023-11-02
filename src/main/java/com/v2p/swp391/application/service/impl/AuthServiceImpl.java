package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.event.MailEvent;
import com.v2p.swp391.application.model.RoleEntity;
import com.v2p.swp391.application.model.Token;
import com.v2p.swp391.application.repository.TokenRepository;
import com.v2p.swp391.application.request.SetPasswordByForgotRequest;
import com.v2p.swp391.application.service.AuthService;
import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.common.constant.Role;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.request.LoginRequest;
import com.v2p.swp391.application.request.SignUpRequest;
import com.v2p.swp391.application.response.AuthResponse;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.notification.impl.MailServiceImpl;
import com.v2p.swp391.security.TokenProvider;
import com.v2p.swp391.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;
    private MailServiceImpl mailService;

    @Value("${app.fe.verify_url}")
    private String verifyUrl;

    @Value("${app.fe.forgot_password_url}")
    private String forgotPasswordUrl;

    public AuthServiceImpl() {
    }

    @Override
    public AuthResponse signIn(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if(!userPrincipal.getUser().getEmailVerified()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Email not verified");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(userPrincipal.getAuthorities().iterator().next().getAuthority())
                .userId(userPrincipal.getId())
                .imageUrl(userPrincipal.getUser().getImageUrl())
                .build();
    }

    @Override
    public void signUp(SignUpRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(user.getEmailVerified()){
                throw new AppException(HttpStatus.BAD_REQUEST,"Email address already in use.");
            }else{
                sendVerifyMail(user);
                return;
            }
        }

        // Creating user's account
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setImageUrl(Image.USER_IMAGE_PATH+Image.DEFAULT_AVATAR);
        user.setEmailVerified(false);
        user.setRoleEntity(RoleEntity.builder().id(Long.valueOf(Role.CUSTOMER)).build());
        user.setAddress(request.getAddress());
        user.setDob(request.getDob());
        user.setPhoneNumber(request.getPhone());

        userRepository.save(user);

        sendVerifyMail(user);

    }

    @Override
    public void verify(Long userId, String token) {
        tokenProvider.validateToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with id : " + userId)
                );
        user.setEmailVerified(true);

        userRepository.save(user);
    }

    @Override
    public String refresh(String token) {
        Token refreshToken = tokenRepository.findByName(token)
                .orElseThrow(() ->
                        new AppException(HttpStatus.BAD_REQUEST,"Refresh Token not found with token : " + token)
                );
        if(refreshToken.isRevoked()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Token đã bị thu hồi");
        }
        if(refreshToken.isExpired()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Token đã hết hạn");
        }
        if(refreshToken.getExpirationDate().isBefore(LocalDate.now())){
            refreshToken.setExpired(true);
            throw new AppException(HttpStatus.BAD_REQUEST,"Token đã hết hạn");
        }

        String accessToken = tokenProvider.createAccessToken(refreshToken.getUser().getId());

        return accessToken;
    }

    private void sendVerifyMail(User user) {
        String token = tokenProvider.createToken(user.getId(), 300000); // 5 minutes
        String urlPattern = verifyUrl + "?userId={0}&token={1}";
        String url = MessageFormat.format(urlPattern, user.getId(), token);
            applicationEventPublisher.publishEvent(new MailEvent(this, user, url, "verify"));
    }

    @Override
    public void sendMailForgotPassword(String email){
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));

        String token = tokenProvider.createToken(user.getId(), 600000); //10 minutes    `
        String urlPattern = forgotPasswordUrl + "?userId={0}&token={1}";
        String url = MessageFormat.format(urlPattern, user.getId(), token);
            applicationEventPublisher.publishEvent(new MailEvent(this, user, url, "forgot"));
    }

    @Override
    public void setPassword(Long userId, String token, SetPasswordByForgotRequest request) {
        tokenProvider.validateToken(token);
        User user = userRepository
                .findById(userId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("User", "id", userId));
        if(request.getPassword().equals(request.getConfirmedPassword())){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        else{
            throw new AppException(HttpStatus.BAD_REQUEST, "Confirmed password is wrong");
        }
        userRepository.save(user);
    }
}
