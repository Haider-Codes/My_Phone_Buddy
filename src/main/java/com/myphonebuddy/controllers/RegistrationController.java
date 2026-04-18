package com.myphonebuddy.controllers;

import com.myphonebuddy.enums.MessageType;
import com.myphonebuddy.modal.Acknowledgement;
import com.myphonebuddy.modal.UserInfo;
import com.myphonebuddy.service.UserService;
import com.myphonebuddy.utility.utils.BuilderUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    // To process/handle the registration
    @PostMapping
    public String processRegister(@Valid @ModelAttribute UserInfo userInfo, BindingResult bindingResult,
                                  HttpSession httpSession) {
        log.info("Processing User Registration");
        log.info("User Information: {}",  userInfo);

        Acknowledgement acknowledgement = null;

        // Validating input data
        if(bindingResult.hasErrors()){
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Please correct invalid data");
            httpSession.setAttribute("acknowledgement", acknowledgement);
            return "register";
        }
        // User is disabled by default.
        var registered_user = BuilderUtil.buildFormUser(userInfo);

        // Saving the user
        var saved_user = userService.saveUser(registered_user);

        if(saved_user != null){
            log.info("User saved successfully");

            // Once signup/registration is successful, we'll provide an success acknowledgement message
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.SUCCESS, "User registered successfully");
        }
        else {
            log.info("Something went wrong while saving user in database");

            // If signup/registration fails, we'll provide an error acknowledgement message
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Something went wrong while saving user.");
        }

        httpSession.setAttribute("acknowledgement", acknowledgement);

        return "redirect:/signUp";
    }

}
