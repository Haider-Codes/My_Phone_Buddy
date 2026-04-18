package com.myphonebuddy.controllers;

import com.myphonebuddy.entity.Contact;
import com.myphonebuddy.enums.MessageType;
import com.myphonebuddy.modal.Acknowledgement;
import com.myphonebuddy.modal.ContactInfo;
import com.myphonebuddy.modal.PageInfo;
import com.myphonebuddy.modal.SearchContactInfo;
import com.myphonebuddy.service.ContactService;
import com.myphonebuddy.service.ImageService;
import com.myphonebuddy.service.UserService;
import com.myphonebuddy.utility.utils.BuilderUtil;
import com.myphonebuddy.utility.utils.LoggedInUserUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/user/contacts")
public class ContactController {

    private final ContactService contactService;

    private final UserService userService;

    private final ImageService imageService;

    public ContactController(ContactService contactService,  UserService userService, ImageService imageService) {
        this.contactService = contactService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/add")
    public String addContact(Model model) {
        log.info("Viewing Add Contact Page.");
        model.addAttribute("contactInfo", new ContactInfo());
        return "user/add_contact";
    }

    // Contact save handler
    @PostMapping("/add")
    public String processContact(@Valid @ModelAttribute ContactInfo contactInfo, BindingResult bindingResult,
                                 Authentication authentication, HttpSession session) {

        Acknowledgement acknowledgement = null;

        // Processing contact details
        log.info("Processing Contact");

        // Validating input data
        if (bindingResult.hasErrors()) {
        // Checking bindingResult errors on console
        //    bindingResult.getAllErrors().forEach(error -> log.info(error.toString()));
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Please correct invalid data");
            session.setAttribute("acknowledgement", acknowledgement);
            return "user/add_contact";
        }

        // Printing image file information
        log.info("Image File Information: {}", contactInfo.getPicture().getOriginalFilename());

        // Generating a random filename for image upload, so to mask the actual name and provide uniqueness.
        // This will even be useful in case of generating a new image url, based on cropping old-image using public_id
        // parameter of Cloudinary.
        String imageFileName = UUID.randomUUID().toString();
        log.info("Image File Name: {}", contactInfo.getPicture().getOriginalFilename());
        var saved_contact = contactService.saveContact(BuilderUtil.buildContact(contactInfo, authentication, userService, imageService, imageFileName));

        if(saved_contact != null) {
            log.info("Contact saved successfully.");
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.SUCCESS, "Contact saved successfully");
        }
        else {
            log.info("Some error occurred while saving contact to database.");
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Something went wrong while saving contact");
        }

        session.setAttribute("acknowledgement", acknowledgement);

        return "redirect:/user/contacts/add";
    }

    // View Contacts route
    @GetMapping
    public String viewContacts(Model model,
                               Authentication authentication,
                               @ModelAttribute PageInfo pageInfo,
                               @RequestParam(defaultValue = "name") String field,
                               @RequestParam(defaultValue = "asc") String direction) {
        log.info("Viewing all contacts page.");

        // Without pagination
        //var contacts = contactService.findAllContactsByUserEmail(LoggedInUserUtil.getLoggedInUser(authentication));

        // With pagination
        var contacts = contactService.findAllContactsByUserEmail(LoggedInUserUtil.getLoggedInUser(authentication),
                                pageInfo.getPageNo(), pageInfo.getPageSize(), field, direction);
        
        if(!contacts.isEmpty()) {
            // Viewing contact
            log.info("Found {} contacts", contacts);
            log.info("Contact Name: {}", contacts.getContent().getFirst().getName());
        }
        // Adding contacts list into the model
        model.addAttribute("contacts", contacts);

        // Adding searchContactInfo to model
        model.addAttribute("searchContactInfo", new SearchContactInfo());

        // Adding a pageInfo model
        model.addAttribute("pageInfo", pageInfo);

        return "user/view_contacts";
    }

    // handling search contacts
    @GetMapping("/search")
    public String searchContact(Model model,
                                Authentication authentication,
                                @ModelAttribute SearchContactInfo searchContactInfo,
                                @ModelAttribute PageInfo pageInfo,
                                @RequestParam(defaultValue = "asc") String direction) {

        log.info("Viewing search contact page.");
        log.info("Searching for contacts with field: {}, keyword: {}", searchContactInfo.getField(), searchContactInfo.getKeyword());

        Page<Contact> searchContacts = null;
        var loggedInUser = LoggedInUserUtil.getLoggedInUser(authentication);

        searchContacts = switch (searchContactInfo.getField()) {
            case "name" ->
                    contactService.searchByName(loggedInUser, pageInfo.getPageNo(), pageInfo.getPageSize(), searchContactInfo.getField(), searchContactInfo.getKeyword(), direction);
            case "email" ->
                    contactService.searchByEmail(loggedInUser, pageInfo.getPageNo(), pageInfo.getPageSize(), searchContactInfo.getField(), searchContactInfo.getKeyword(), direction);
            case "phoneNumber" ->
                    contactService.searchByPhoneNumber(loggedInUser, pageInfo.getPageNo(), pageInfo.getPageSize(), searchContactInfo.getField(), searchContactInfo.getKeyword(), direction);
            default ->
                    contactService.searchByDescription(loggedInUser, pageInfo.getPageNo(), pageInfo.getPageSize(), searchContactInfo.getField(), searchContactInfo.getKeyword(), direction);
        };

        if(!searchContacts.isEmpty()) {
            log.info("Search Contact is: {}", searchContacts.getContent().getFirst().getName());
        }
        model.addAttribute("searchContactInfo", searchContactInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("searchContacts", searchContacts);

        return "user/search_contact";
    }

    @GetMapping("/delete")
    public String deleteContact(@RequestParam UUID contactId, HttpSession session, Authentication authentication) {
        log.info("Deleting contact with id: {}", contactId);

        // Grabbing loggedIn user
        var loggedInUser = LoggedInUserUtil.getLoggedInUser(authentication);

        var contact = contactService.findContactById(loggedInUser, contactId);

        var acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.SUCCESS, "Contact deleted successfully");
        contact.ifPresent(contactService::deleteContact);
        session.setAttribute("acknowledgement", acknowledgement);

        log.info("Contact with id: {} deleted successfully", contactId);

        return "redirect:/user/contacts";
    }

    @GetMapping("/update/{contactId}")
    public String updateContact(@PathVariable UUID contactId, Model model, Authentication authentication) {

        log.info("Fetched contact {} information for update", contactId);

        // Grabbing loggedIn user
        var loggedInUser = LoggedInUserUtil.getLoggedInUser(authentication);

        // Grabbing existing contact information
        var ex_contact = contactService.findContactById(loggedInUser, contactId);

        // Building a contactInfo object to be sent as a model to the update view.
        ContactInfo contactInfo = new ContactInfo();
        if(ex_contact.isPresent()) {
            contactInfo.setName(ex_contact.get().getName());
            contactInfo.setEmail(ex_contact.get().getEmail());
            contactInfo.setAddress(ex_contact.get().getAddress());
            contactInfo.setPhoneNumber(ex_contact.get().getPhoneNumber());
            contactInfo.setDescription(ex_contact.get().getDescription());
            contactInfo.setFavourite(ex_contact.get().isFavourite());
            contactInfo.setWebsiteLink(ex_contact.get().getWebsiteLink());
            contactInfo.setLinkedInLink(ex_contact.get().getLinkedInLink());
            contactInfo.setPictureUrl(ex_contact.get().getPicture());
            contactInfo.setPicturePublicId(ex_contact.get().getPicturePublicId());
        }

        model.addAttribute("contactInfo", contactInfo);
        model.addAttribute("contactId", contactId);
        return "user/update_contact";
    }

    @PostMapping("/update/{contactId}")
    public String processContactUpdate(@PathVariable(name = "contactId") UUID contactId,
                                       @Valid @ModelAttribute ContactInfo contactInfo,
                                       BindingResult bindingResult,
                                       Authentication authentication, HttpSession session) {

        log.info("Updating contact with id: {}", contactId);

        Contact updated_contact = null;
        Acknowledgement acknowledgement = null;
        String imageFileName = null;

        if(bindingResult.hasErrors()) {
            acknowledgement = BuilderUtil.buildAcknowledgementMsg(MessageType.ERROR, "Please correct invalid data");
            session.setAttribute("acknowledgement", acknowledgement);
            return "user/update_contact";
        }

        // Grabbing loggedIn user
        var loggedInUser = LoggedInUserUtil.getLoggedInUser(authentication);

        // Grabbing existing contact information
        var ex_contact = contactService.findContactById(loggedInUser, contactId);

        // logging picture information
        log.info("contact image is: {}", contactInfo.getPicture().getOriginalFilename());
        log.info("is contact image empty? {}",  contactInfo.getPicture().isEmpty());

        // Check for contact image
        if(ex_contact.isPresent()) {
            if(!contactInfo.getPicture().isEmpty()) {
                imageFileName = UUID.randomUUID().toString();
                updated_contact = BuilderUtil.buildContact(contactInfo, authentication, userService,
                        imageService, imageFileName);
            }
            else {
                imageFileName = ex_contact.get().getPicturePublicId();
                updated_contact = BuilderUtil.buildContact(contactInfo, authentication, userService,
                        imageService, imageFileName);
                updated_contact.setPicture(ex_contact.get().getPicture());
            }
            updated_contact.setId(ex_contact.get().getId());
        }
        else {
            log.info("Contact with id: {} not present", contactId);
        }

        if(contactService.updateContact(updated_contact) != null) {
            log.info("Updated contact with email: {}", contactInfo.getEmail());
            acknowledgement = Acknowledgement.builder()
                    .type(MessageType.SUCCESS)
                    .message("Contact updated successfully")
                    .build();
        }
        else {
            log.info("Some error happened while updating contact with email: {}", contactInfo.getEmail());
            acknowledgement = Acknowledgement.builder()
                    .type(MessageType.ERROR)
                    .message("Some error happened while updating contact")
                    .build();
        }

        session.setAttribute("acknowledgement", acknowledgement);
        return "redirect:/user/contacts/update/" + contactId ;
    }

}
