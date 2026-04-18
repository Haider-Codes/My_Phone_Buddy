package com.myphonebuddy.handler;

import com.myphonebuddy.entity.User;
import com.myphonebuddy.enums.Provider;
import com.myphonebuddy.service.UserService;
import com.myphonebuddy.utility.utils.BuilderUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public OAuthSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    // This is useful to handle the data after a successful OAuth2 login.
    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuthUser Principle is {}", Objects.requireNonNull(authentication.getPrincipal()).toString());

        log.info("OAuthUser Details is {}", Objects.requireNonNull(authentication.getDetails()).toString());

        // Capturing loggedIn User
        var loggedInUser = (DefaultOAuth2User) authentication.getPrincipal();

        // Capturing loggedIn User OAuthToken. It contains 'AuthorizedClientRegistrationId' for validating the OAuth2 Provider)
        var oauth2Token = (OAuth2AuthenticationToken) authentication;

        // Initialized the User object
        User oAuthUser = new User();

        // Now, dynamically fetching attributes provider by the corresponding OAuth2 providers.
        if(oauth2Token.getAuthorizedClientRegistrationId().equalsIgnoreCase("google")) {

            // Building a 'User' entity object to save to database.
            oAuthUser = getOAuthUser(loggedInUser, Provider.GOOGLE);
        }

        if(oauth2Token.getAuthorizedClientRegistrationId().equalsIgnoreCase("github")) {

            // Building a 'User' entity object to save to database.
            oAuthUser = getOAuthUser(loggedInUser, Provider.GITHUB);
        }

        // Similarly we can log and check the attributes provided by other OAuthProviders, such as Facebook/Meta, LinkedIn, etc.

        // Saving user to database if not exist
        if(userService.findUserByEmail(oAuthUser.getEmail()).isEmpty()) {
            userService.saveUser(oAuthUser);
            log.info("OAuth User {} has been saved successfully", oAuthUser.getEmail());
        }
        else
            log.info("OAuth User {} already exist in database.", oAuthUser.getEmail());

        response.sendRedirect("/user/dashboard");
    }

    private User getOAuthUser(DefaultOAuth2User user, Provider oauthProvider) {

        // Capturing OAuth-2 User details after successful OAuth-2 Login, and building JPA User object.
        // We're adding a null check becoz if the email-address for some user in GitHub is private, 'email' attribute will be null.
        var email = user.getAttribute("email") != null ? Objects.requireNonNull(user.getAttribute("email")).toString() : Objects.requireNonNull(user.getAttribute("login")) +"@gmail.com";
        var name = Objects.requireNonNull(user.getAttribute("name")).toString();
        var profilePicture = oauthProvider == Provider.GOOGLE ? Objects.requireNonNull(user.getAttribute("picture")).toString() : Objects.requireNonNull(user.getAttribute("avatar_url")).toString();
        var providerUserId = oauthProvider == Provider.GOOGLE ? user.getName() :Objects.requireNonNull(user.getAttribute("login")).toString();
        return BuilderUtil.buildOAuthUser(oauthProvider, email, name, profilePicture, providerUserId);
    }
}
