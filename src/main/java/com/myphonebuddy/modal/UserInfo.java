package com.myphonebuddy.modal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserInfo {

    @NotBlank(message = "First Name is required")
    @Size(min = 3, message = "Minimum 3 characters should be present in first-name")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    @Size(min = 3, message = "Minimum 3 characters should be present in last-name")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Not a valid email-address")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Minimum eight characters are required in a password")
    private String password;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max=12, message = "Minimum 10 and maximum 12 digits are required in a phone number")
    private String phoneNumber;
    @NotBlank(message = "About is required")
    private String about;
    private String profilePic; // for image url
    private MultipartFile picture; // for input image

}
