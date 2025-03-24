package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;

@Controller
public class LoginController {

    @Autowired
    AppUserService appUserService;

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
            Model model, RedirectAttributes redirectAttributes) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean usernameExists = true;
        boolean passwordsMatch = true;

        AppUser existingUser = appUserService.getAppUserByUsername(username).orElse(null);
        if (existingUser == null) {
            // Username does not exist, return login page again
            usernameExists = false;
            redirectAttributes.addFlashAttribute("usernameExists", usernameExists);
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            // Password does not match saved one
            passwordsMatch = false;
            redirectAttributes.addFlashAttribute("passwordsMatch", passwordsMatch);
            return "redirect:/login";
        }

        // User exists and login correct
        System.out.println("Working: " + existingUser);
        model.addAttribute("user", existingUser);
        return "redirect:/joinFlat";
    }
}
