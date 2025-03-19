package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;

@Controller
public class CreateAccountController {

    @Autowired
    AppUserService appUserService;

    @PostMapping("/createAccount")
    public String createAccount(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
            @RequestParam("username") String username, @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (password.equals(confirmPassword)) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            AppUser newUser = new AppUser(firstName, lastName, username, passwordEncoder.encode(password));
            System.out.println("Working: " + newUser);
            appUserService.saveAppUser(newUser);
        }

        return "login";
    }
}
