package com.v2p.swp391.application.event;

import com.v2p.swp391.application.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class MailEvent  extends ApplicationEvent {

    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private String Url;

    @Getter
    @Setter
    private String type;

    public MailEvent(Object source, User user, String url, String type) {
        super(source);
        this.user = user;
        Url = url;
        this.type = type;
    }
}
