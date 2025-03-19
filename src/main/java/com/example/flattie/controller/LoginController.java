package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;

@Controller
public class LoginController {

    @Autowired
    AppUserService appUserService;

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        AppUser existingUser = appUserService.getAppUserByUsername(username).orElse(null);
        if (existingUser == null) {
            // Username does not exist, return login page again
            return "login";
        }
        if (passwordEncoder.encode(password).equals((existingUser.getPassword()))) {
            System.out.println("Working: " + existingUser);
            return "joinFlat";
        } else {
            // Password does not match saved one
            return "login";
        }

    }
}
