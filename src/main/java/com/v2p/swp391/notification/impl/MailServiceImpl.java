package com.v2p.swp391.notification.impl;

import com.v2p.swp391.application.event.MailEvent;
import com.v2p.swp391.notification.ThymeleafService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import com.v2p.swp391.notification.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MailServiceImpl implements MailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Value("${app.mail.host}")
    private String host;
    @Value("${app.mail.port}")
    private String port;
    @Value("${app.mail.username}")
    private String email;
    @Value("${app.mail.password}")
    private String password;

    @Autowired
    ThymeleafService thymeleafService;

    @Async
    @EventListener
    @Override
    public void sendVerifyMail(MailEvent event) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
        Message message = new MimeMessage(session);
        try {
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(event.getUser().getEmail())});

            message.setFrom(new InternetAddress(email));
            message.setSubject("Xác thực tài khoản Bird Farm Shop");
            message.setContent(thymeleafService.getVerifyContent(event.getUser(),event.getVerificationUrl()), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
