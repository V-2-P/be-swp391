package com.v2p.swp391.security.oauth2;

import com.v2p.swp391.application.model.SocialAccount;
import com.v2p.swp391.application.model.SocialProvider;
import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.repository.SocialAccountRepository;
import com.v2p.swp391.application.repository.UserRepository;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.security.UserPrincipal;
import com.v2p.swp391.security.oauth2.user.OAuth2UserInfo;
import com.v2p.swp391.security.oauth2.user.OAuth2UserInfoFactory;
import com.v2p.swp391.exception.OAuth2AuthenticationProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SocialAccountRepository socialAccountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User)  {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        Optional<SocialAccount> socialAccountOptional = socialAccountRepository.findBySocialId(oAuth2UserInfo.getId());
        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        SocialAccount socialAccount;
        if (socialAccountOptional.isPresent()) {
            socialAccount = socialAccountOptional.get();
            user = findExistingUser(socialAccount, oAuth2UserInfo);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication!= null){
                user = linkedUser(oAuth2UserRequest, oAuth2UserInfo);

            }else{
                throw new OAuth2AuthenticationProcessingException("Account not linked.");

            }
        }



        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User linkedUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseThrow(() -> new OAuth2AuthenticationProcessingException( "Can not find email." ));

        SocialAccount socialAccount = new SocialAccount();
        socialAccount.setSocialId(oAuth2UserInfo.getId());
        socialAccount.setUser(user);
        socialAccount.setProvider(SocialProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        return userRepository.save(user);
    }

    private User findExistingUser(SocialAccount existingSocialAccount, OAuth2UserInfo oAuth2UserInfo) {
        User existingUser = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseThrow(() -> new OAuth2AuthenticationProcessingException( "Can not find email." ));
        return userRepository.save(existingUser);
    }

}