package com.myphonebuddy.controllers;

import com.myphonebuddy.modal.FeedbackInfo;
import com.myphonebuddy.modal.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class PageController {

    @RequestMapping("/")
    public String indexPage() {
        return "redirect:/home";
    }

    // home page routing
    @RequestMapping("/home")
    public String homePage(Model model) {
        log.info("Home Page handler");
        // sending data to view
        model.addAttribute("name", "My Phone Buddy Application");
        model.addAttribute("activePage", "home");
        model.addAttribute("description", "A one stop solution to manage your contacts");
        return "index";
    }

    // about page routing
    @RequestMapping("/about")
    public String aboutPage(Model model) {
        log.info("About Page");
        
        // Testing fragment
        model.addAttribute("isSignedIn", true);
        model.addAttribute("activePage", "about");
        return "about";
    }

    // service page routing
    @RequestMapping("/services")
    public String servicesPage(Model model) {
        log.info("Services Page");
        model.addAttribute("activePage", "services");
        return "services";
    }

    // contact page routing
    @RequestMapping("/contact")
    public String contactPage(Model model) {
        log.info("Contact Us Page");
        model.addAttribute("feedbackInfo", new FeedbackInfo());
        model.addAttribute("activePage", "contact");
        return "contact";
    }

    // register page routing
    @RequestMapping("/signUp")
    public String registerPage(Model model) {
        log.info("Sign Up Page");
        model.addAttribute("userInfo", new UserInfo());
        return "register";
    }

    // login page routing
    @RequestMapping("/login")
    public String loginPage() {
        log.info("Login Page");
        return "login";
    }

}
