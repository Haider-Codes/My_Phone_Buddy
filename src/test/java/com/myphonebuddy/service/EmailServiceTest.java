package com.myphonebuddy.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@Slf4j
public class EmailServiceTest {

    private final EmailService emailService;

    @Autowired
    public EmailServiceTest(EmailService emailService) {
        this.emailService = emailService;
    }

    @Disabled // Not required, just for testing email service
    @Test
    public void sendEmail() throws IOException {
        log.info("Sending email...");
        emailService.sendEmail(
                "haiderraza786110@gmail.com",
                "Test Email",
                "This is a testing email service."
        );
    }

}
