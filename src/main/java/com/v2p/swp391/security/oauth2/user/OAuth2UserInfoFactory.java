package com.v2p.swp391.security.oauth2.user;

import com.v2p.swp391.application.model.SocialProvider;
import com.v2p.swp391.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(SocialProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(SocialProvider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        }  else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
