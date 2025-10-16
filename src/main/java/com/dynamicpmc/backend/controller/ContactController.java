package com.dynamicpmc.backend.controller;

import com.dynamicpmc.backend.dto.ContactFormDto;
import com.dynamicpmc.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody ContactFormDto contactForm) {
        try {
            emailService.sendContactEmail(
                contactForm.getName(),
                contactForm.getEmail(),
                contactForm.getPhone(),
                contactForm.getMessage()
            );
            return ResponseEntity.ok("Message sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to send message.");
        }
    }
}