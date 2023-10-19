package com.v2p.swp391.application.event;

import com.v2p.swp391.application.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class MailEvent  extends ApplicationEvent {

    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private String verificationUrl;

    public MailEvent(Object source, User user, String verificationUrl) {
        super(source);
        this.user = user;
        this.verificationUrl = verificationUrl;
    }
}
