package com.myphonebuddy.controllers;

import com.myphonebuddy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService  userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {

        log.info("Verifying email...");

        var ex_user = userService.findUserByToken(token);

        if(ex_user.isPresent()) {

            ex_user.get().setEmailVerified(true);
            ex_user.get().setEnabled(true);
            userService.updateUser(ex_user.get());

            // returning a 'success' view
            return "verification_success";

        }
        else {
            return "verification_failed";
            // return a 'error' view
        }
    }

}
