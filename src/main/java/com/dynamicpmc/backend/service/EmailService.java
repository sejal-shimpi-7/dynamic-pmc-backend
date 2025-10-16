package com.dynamicpmc.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendContactEmail(String name, String fromEmail, String phone, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo("admin@dynamicpmc.com"); 
        mailMessage.setSubject("New Contact Form Submission from: " + name);

        String emailBody = "You have received a new message from your website's contact form.\n\n" +
                           "Name: " + name + "\n" +
                           "Email: " + fromEmail + "\n" +
                           "Phone: " + phone + "\n\n" +
                           "Message:\n" + message;

        mailMessage.setText(emailBody);

        mailSender.send(mailMessage);
    }
}