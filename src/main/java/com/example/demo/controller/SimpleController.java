package com.example.demo.controller;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/**")
public class SimpleController {

    private final JavaMailSender mailSender;

    public SimpleController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PreAuthorize("hasAnyRole('GROUP1')")
    @GetMapping
    public String index() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
