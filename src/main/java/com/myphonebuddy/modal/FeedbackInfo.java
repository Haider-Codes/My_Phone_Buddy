package com.myphonebuddy.modal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackInfo {

    @NotBlank
    @Size(min = 3, message = "Minimum 3 characters are required in the name")
    private String name;
    @Email(message = "Not a valid email-address")
    private String email;
    @NotBlank
    @Size(min=10, max = 10000, message = "Message should not be empty")
    private String message;

}
