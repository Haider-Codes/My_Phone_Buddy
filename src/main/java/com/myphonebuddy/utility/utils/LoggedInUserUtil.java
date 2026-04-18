package com.myphonebuddy.utility.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class LoggedInUserUtil {

    public static String getLoggedInUser(Authentication authentication) {

        String user = null;
        // Java Pattern Matching (introduced in Java-16), It checks if the authentication object from SecurityContextHolder
        // is an instanceOf OAuth2AuthenticationToken, then it will automatically create a local variable 'oauthToken'
        if(authentication instanceof OAuth2AuthenticationToken oauthToken) {

            if (oauthToken.getAuthorizedClientRegistrationId().equalsIgnoreCase("google")) {

                log.info("User Logged in through google");
                user = Objects.requireNonNull(Objects.requireNonNull(oauthToken.getPrincipal()).getAttributes().get("email")).toString();
            }
            if (oauthToken.getAuthorizedClientRegistrationId().equalsIgnoreCase("github")) {

                log.info("User Logged in through github");
                user = Objects.requireNonNull(oauthToken.getPrincipal()).getAttributes().get("login") + "@gmail.com";
            }
        }
        else {
            log.info("User is logged in through database");
            user = authentication.getName();
        }
        return user;
    }
}
