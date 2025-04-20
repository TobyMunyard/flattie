package com.example.flattie.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMaintenanceEmail(String to, String subject, String body, File imageAttachment) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // plain text

            if (imageAttachment != null && imageAttachment.exists()) {
                helper.addAttachment(imageAttachment.getName(), imageAttachment);
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("[EmailService] Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
