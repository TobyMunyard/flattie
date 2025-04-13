package com.example.flattie.config;

import com.example.flattie.model.AppUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserAdvice {

    @ModelAttribute("currentUser")
    public AppUser populateCurrentUser(@AuthenticationPrincipal AppUser user) {
        return user;
    }
}
