package com.v2p.swp391.notification;

import com.v2p.swp391.application.model.User;

public interface ThymeleafService {
    String getVerifyContent(User user, String url);

    String getResetPasswordContent(User user, String url);
    String getPaymentContent(User user, String url);
    String getShippingContent(User user, String message);
}
