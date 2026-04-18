package com.myphonebuddy.controllers;

import com.myphonebuddy.constants.AppConstants;
import com.myphonebuddy.entity.Activity;
import com.myphonebuddy.entity.Contact;
import com.myphonebuddy.entity.User;
import com.myphonebuddy.enums.MessageType;
import com.myphonebuddy.enums.Provider;
import com.myphonebuddy.modal.Acknowledgement;
import com.myphonebuddy.modal.UserInfo;
import com.myphonebuddy.service.ImageService;
import com.myphonebuddy.service.UserService;
import com.myphonebuddy.utility.utils.BuilderUtil;
import com.myphonebuddy.utility.utils.LoggedInUserUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final ImageService imageService;

    public UserController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    // User Dashboard View
    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        log.info("Viewing user dashboard");

        // Grabbed logged-in user
        var loggedInUser = userService.findUserByEmail(LoggedInUserUtil.getLoggedInUser(authentication));

        List<Contact> favouriteContacts = null;
        List<Contact> topFavouriteContacts = null;
        List<Contact> recentlyAddedContacts = null;
        List<Contact> addedContactsWithTime = null;
        List<Contact> addedFavouriteContactsWithTime = null;
        List<Activity> recentActivities = null;

        // Fetching user favourite contacts
        if(loggedInUser.isPresent()) {
            favouriteContacts = getFavouriteContacts(loggedInUser.get(), loggedInUser.get().getContacts().size()); // All favourite contacts
            topFavouriteContacts = getFavouriteContacts(loggedInUser.get(), AppConstants.TOP_FAVOURITE_CONTACTS); // top 5 contacts
            recentlyAddedContacts = getRecentContacts(loggedInUser.get()); // Recently added 5 contacts
            addedContactsWithTime = getAddedContactsWithTime(loggedInUser.get()); // Fetching contacts added w.r.t time
            addedFavouriteContactsWithTime = getFavouriteContactsWithTime(loggedInUser.get()); // Fetching favourite contacts added w.r.t time
            recentActivities = getRecentActivityWithTime(loggedInUser.get());
        }

        model.addAttribute("favouriteContacts", favouriteContacts);
        model.addAttribute("topFavouriteContacts", topFavouriteContacts);
        model.addAttribute("recentlyAddedContacts", recentlyAddedContacts);
        model.addAttribute("addedContactsWithTime", addedContactsWithTime);
        model.addAttribute("addedFavouriteContactsWithTime", addedFavouriteContactsWithTime);
        model.addAttribute("recentActivities", recentActivities);

        return "user/dashboard";
    }

    // User Profile View
    @GetMapping("/profile")
    public String userProfile() {
        log.info("Viewing user profile");
        return "user/profile";
    }

    @GetMapping("/profile/update")
    public String updateUser(@RequestParam String email, Model model) {
        log.info("Fetched user profile with email: {}", email);

        var ex_user =  userService.findUserByEmail(email);
        UserInfo userInfo = new UserInfo();

        if(ex_user.isPresent()) {
            userInfo.setFirstName(ex_user.get().getFirstName());
            userInfo.setLastName(ex_user.get().getLastName());
            userInfo.setEmail(ex_user.get().getEmail());
            userInfo.setPassword(ex_user.get().getProvider() == Provider.SELF ? ex_user.get().getPassword() : null);
            userInfo.setAbout(ex_user.get().getAbout());
            userInfo.setProfilePic(ex_user.get().getProfilePic());
            userInfo.setPhoneNumber(ex_user.get().getPhoneNumber());
        }

        model.addAttribute("userInfo", userInfo);
        return "user/update_profile";
    }

    @PostMapping("/profile/update")
    public String processUserUpdate(@RequestParam String email,
                                    @Valid @ModelAttribute UserInfo userInfo,
                                    BindingResult bindingResult,
                                    HttpSession session) {

        log.info("Updating user profile with email: {}", email);

        Acknowledgement acknowledgement = null;
        User updated_user = null;
        String profileImageName = null;

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach((error) -> {
                log.info("error: {}", error.getDefaultMessage());
            });
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Please correct invalid data");
            session.setAttribute("acknowledgement", acknowledgement);
            return "user/update_profile";
        }

        var ex_user =  userService.findUserByEmail(email);

        if(ex_user.isPresent()) {
           if(userInfo.getPicture() != null && !userInfo.getPicture().isEmpty()) {
               profileImageName = UUID.randomUUID().toString();
               updated_user = BuilderUtil.buildUpdatedUser(userInfo, imageService, profileImageName);
           }
           else {
               updated_user = BuilderUtil.buildUpdatedUser(userInfo, imageService, profileImageName);
               updated_user.setProfilePic(ex_user.get().getProfilePic());
           }

           // Handling non-editable fields
           updated_user.setEnabled(ex_user.get().isEnabled());
           updated_user.setEmailVerified(ex_user.get().isEmailVerified());
           updated_user.setPhoneVerified(ex_user.get().isPhoneVerified());
           updated_user.setProviderUserId(ex_user.get().getProviderUserId());
           updated_user.setProvider(ex_user.get().getProvider());
           updated_user.setEmail(ex_user.get().getEmail());
        }
        else {
            log.info("No user found with email: {}", email);
        }

        if(userService.updateUser(updated_user) != null) {
            log.info("User: {} successfully updated",  email);
            acknowledgement =  BuilderUtil.buildAcknowledgementMsg(MessageType.SUCCESS, "User updated successfully");
        }
        else {
            log.info("Some error happened while updating contact with email: {}", email);
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Some error happened while updating the user");
        }

        session.setAttribute("acknowledgement", acknowledgement);

        return "redirect:/user/profile/update?email="+email;

    }

    private List<Contact> getFavouriteContacts(User user, int maxSize) {

        return user.getContacts()
                    .stream()
                    .filter(Contact::isFavourite)
                    .limit(maxSize)
                    .toList();

    }

    private List<Contact> getRecentContacts(User user) {
        return user.getContacts()
                .stream()
                .skip(user.getContacts().size() >= AppConstants.RECENTLY_ADD_CONTACTS ?
                        user.getContacts().size() - AppConstants.RECENTLY_ADD_CONTACTS :
                        0)
                .toList();
    }

    private List<Contact> getAddedContactsWithTime(User user) {

        var date = LocalDateTime.now().minusHours(1); // Last Hour

        return user.getContacts()
                .stream()
                .filter(contact -> contact.getCreatedAt().isAfter(date))
                .toList();
    }

    private List<Contact> getFavouriteContactsWithTime(User user) {

        var date = LocalDateTime.now().minusHours(1); // Last Hour

        return user.getContacts()
                .stream()
                .filter(contact -> contact.getCreatedAt().isAfter(date))
                .filter(Contact::isFavourite)
                .toList();
    }

    private List<Activity> getRecentActivityWithTime(User user) {

        var date = LocalDateTime.now().minusHours(1); // Last Hour

        return user.getActivities()
                .stream()
                .filter(activity -> activity.getCreatedAt().isAfter(date))
                .skip(user.getActivities().size() >= AppConstants.RECENT_ACTIVITY ?
                        user.getActivities().size() - AppConstants.RECENT_ACTIVITY
                        : 0)
                .toList();
    }

}
