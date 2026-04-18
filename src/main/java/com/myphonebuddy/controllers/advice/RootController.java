package com.myphonebuddy.controllers.advice;

import com.myphonebuddy.entity.Contact;
import com.myphonebuddy.service.UserService;
import com.myphonebuddy.utility.utils.LoggedInUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Slf4j
public class RootController {

    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    // We need to model "loggedInUser" for all views once user logged in, hence used ControllerAdvice
    @ModelAttribute
    public void fetchLoggedInUserDetails(Model model, Authentication authentication) {

        if(authentication == null) {
            return;
        }

        log.info("Logged in User is: {}", LoggedInUserUtil.getLoggedInUser(authentication));
        // Fetching details of loggedIn user from database
        var loggedInUser = userService.findUserByEmail(LoggedInUserUtil.getLoggedInUser(authentication));
        // Added LoggedInUserDetails into the model
        log.info("Added LoggedInUserDetails into the model");
        loggedInUser.ifPresent(user -> model.addAttribute("loggedInUser", user));

    }

}
