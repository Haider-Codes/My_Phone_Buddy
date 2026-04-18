package com.myphonebuddy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final String domainName;

    public EmailServiceImpl(JavaMailSender mailSender,
                            @Value("${spring.mail.properties.mail.domain.name}") String domainName) {
        this.mailSender = mailSender;
        this.domainName = domainName;
    }

    @Override
    public void sendEmail(String to, String subject, String content) {

        // Creating a SimpleMailMessage object
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("verify.phonebuddy@"+domainName);

        // sending the email
        mailSender.send(message);

    }
}
