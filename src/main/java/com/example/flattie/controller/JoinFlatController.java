package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;
import com.example.flattie.model.Flat;
import com.example.flattie.service.FlatService;

@Controller
public class JoinFlatController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private FlatService flatService;

    @PostMapping("/joinFlat")
public String joinFlat(@AuthenticationPrincipal AppUser user, @RequestParam("flat_code") String flatCode, Model model) {
    if (user == null) {
        return "redirect:/login"; // Not logged in? Send them to login.
    }

    // Retrieve the flat by its join code
    Flat flat = flatService.findByJoinCode(flatCode);

    if (flat == null) {
        // If no flat is found, add an error message
        model.addAttribute("error", "Invalid flat code. Please try again.");
        return "joinFlat"; // Render the same page with error message
    }

    // Update the user's flat association in the database
    appUserService.joinFlat(user, flat);

    // Redirect to the Flat Info page with the flat ID
    return "redirect:/showFlatInfo?flatId=" + flat.getId();
    }
}