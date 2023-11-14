package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.event.MailEvent;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.repository.TokenRepository;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.application.service.SendEmailService;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.notification.impl.MailServiceImpl;
import com.v2p.swp391.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl  implements SendEmailService {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;
    private MailServiceImpl mailService;

    @Value("${app.fe.verify_url}")
    private String verifyUrl;

    @Value("${app.fe.forgot_password_url}")
    private String forgotPasswordUrl;

    @Override
    public void sendMailPayment(User user, String url) {
        applicationEventPublisher.publishEvent(new MailEvent(this, user, url, "payment"));
    }

    @Override
    public void sendShippingNotificationBooking(User user) {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currentDate = LocalDateTime.now();
        applicationEventPublisher.publishEvent(new MailEvent(this, user, "The fledgling birds from booking was sent at " + sdf.format(currentDate) + "\nYour fledgling birds from booking will be delivered within 5-10 days", "shipping"));
    }


}
