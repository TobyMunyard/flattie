package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.flattie.service.EmailService;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test-email")
    public String testEmail() {
        emailService.sendEmail(
                "fakeAddress@gmail.com",
                "Test from Flattie",
                "This is a test email from your system.");
        return "Email attempted to send.";
    }
}
