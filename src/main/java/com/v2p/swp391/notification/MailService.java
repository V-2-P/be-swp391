package com.v2p.swp391.notification;

import com.v2p.swp391.application.event.MailEvent;

public interface MailService {
    void sendVerifyMail(MailEvent event);
}
