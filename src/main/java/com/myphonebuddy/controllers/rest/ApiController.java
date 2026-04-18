package com.myphonebuddy.controllers.rest;

import com.myphonebuddy.entity.Contact;
import com.myphonebuddy.exception.ContactNotFoundException;
import com.myphonebuddy.service.ContactService;
import com.myphonebuddy.utility.utils.LoggedInUserUtil;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

// Defining a REST Controller to retrieve view contacts by using AJAX to process the JSON data asynchronously.
@RestController
@RequestMapping("/api")
public class ApiController {

    private final ContactService contactService;

    public ApiController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public Optional<Contact> findContactByName(@RequestParam UUID id, Authentication authentication) {

        // Grabbing loggedIn user
        var loggedInUser = LoggedInUserUtil.getLoggedInUser(authentication);

        return Optional.of(contactService.findContactById(loggedInUser, id).orElseThrow(() -> new ContactNotFoundException("No Contact found")));
    }

}
