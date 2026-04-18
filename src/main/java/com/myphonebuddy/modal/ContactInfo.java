package com.myphonebuddy.modal;

import com.myphonebuddy.utility.validator.ValidFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ContactInfo {

    @NotBlank(message = "Name of the contact is required")
    @Size(min = 3, message = "Minimum 3 characters are required in the name")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Not a valid email-address")
    private String email;
    private String description;
    @NotBlank(message = "Phone Number of the contact is required")
    @Size(min = 10, max = 12, message = "Phone number should contain minimum 10 and maximum 12 digits")
    private String phoneNumber;
    @ValidFile(message = "Only files of type jpeg/png with max 2MB size is allowed")
    private MultipartFile picture;
    private String pictureUrl;  // Required to display picture to the user while updating the contact
    private String picturePublicId; // Required to overwrite the old picture if contact photo is updated by user
    private String address;
    private boolean favourite;
    private String websiteLink;
    private String linkedInLink;
}
