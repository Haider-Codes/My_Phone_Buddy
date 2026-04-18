package com.myphonebuddy.utility.utils;

import com.myphonebuddy.entity.Contact;
import com.myphonebuddy.entity.Feedback;
import com.myphonebuddy.entity.User;
import com.myphonebuddy.enums.MessageType;
import com.myphonebuddy.enums.Provider;
import com.myphonebuddy.modal.Acknowledgement;
import com.myphonebuddy.modal.ContactInfo;
import com.myphonebuddy.modal.FeedbackInfo;
import com.myphonebuddy.modal.UserInfo;
import com.myphonebuddy.service.FeedbackService;
import com.myphonebuddy.service.ImageService;
import com.myphonebuddy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class BuilderUtil {

    public static User buildFormUser(UserInfo userInfo) {
        // We haven't added default values, which we have added in User entity
        return User.builder()
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .email(userInfo.getEmail())
                .password(userInfo.getPassword())
                .about(userInfo.getAbout())
                .phoneNumber(userInfo.getPhoneNumber())
                .profilePic("link to the user profile")
                .build();
    }

    public static Acknowledgement buildAcknowledgementMsg(MessageType type, String message) {
        return Acknowledgement.builder()
                .type(type == MessageType.SUCCESS ? MessageType.SUCCESS : MessageType.ERROR)
                .message(message)
                .build();
    }

    public static User buildOAuthUser(Provider oauthProvider,
                                              String email, String name, String profilePicture,
                                      String providerUserId) {

        return User.builder()
                .firstName(name.substring(0, name.lastIndexOf(' ')))
                .lastName(name.substring(name.lastIndexOf(' ')))
                .email(email)
                .provider(oauthProvider)
                .providerUserId(providerUserId)
                .profilePic(profilePicture)
                .password("dummy") // Not required, since user logged-in via oauth provider
                .about("This account is created via " + oauthProvider)
                .isEmailVerified(true)
                .isEnabled(true)
                .profilePic(profilePicture)
                .build();
    }

    public static User buildUpdatedUser(UserInfo userInfo, ImageService imageService, String profileImageName) {

        return User.builder()
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .password(userInfo.getPassword() != null && !userInfo.getPassword().isEmpty() ? userInfo.getPassword() : "dummy")
                .phoneNumber(userInfo.getPhoneNumber() != null ? userInfo.getPhoneNumber() : null)
                .about(userInfo.getAbout())
                .profilePic(!userInfo.getPicture().isEmpty() ?
                        imageService.uploadImage(userInfo.getPicture(), profileImageName) : null)
                .build();

    }

    public static Contact buildContact(ContactInfo contactInfo, Authentication authentication,
                                       UserService userService, ImageService imageService, String imageFileName) {

        Optional<User> loggedInUser = userService.findUserByEmail(LoggedInUserUtil.getLoggedInUser(authentication));

        return Contact.builder()
                .name(contactInfo.getName())
                .email(contactInfo.getEmail())
                .phoneNumber(contactInfo.getPhoneNumber())
                .picture(!contactInfo.getPicture().isEmpty() ? imageService.uploadImage(contactInfo.getPicture(), imageFileName) : null)
                .picturePublicId(imageFileName)
                .address(contactInfo.getAddress())
                .description(contactInfo.getDescription())
                .websiteLink(contactInfo.getWebsiteLink())
                .linkedInLink(contactInfo.getLinkedInLink())
                .isFavourite(contactInfo.isFavourite())
                .user(loggedInUser.orElse(null))
                .build();
    }

    public static Feedback buildFeedback(FeedbackInfo feedbackInfo) {

        return Feedback.builder()
                .name(feedbackInfo.getName())
                .email(feedbackInfo.getEmail())
                .message(feedbackInfo.getMessage())
                .build();
    }
}
