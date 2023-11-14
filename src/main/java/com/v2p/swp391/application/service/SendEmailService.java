package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.User;

public interface SendEmailService {
    public void sendMailPayment(User user, String url);
    public void sendShippingNotificationBooking(User user);
}
