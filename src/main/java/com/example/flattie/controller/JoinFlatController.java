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

import jakarta.servlet.http.HttpSession;

@Controller
public class JoinFlatController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private FlatService flatService;

    @PostMapping("/joinFlat")
    public String joinFlat(@AuthenticationPrincipal AppUser user, @RequestParam("flat_code") String flatCode, Model model) {
        // Retrieve the flat by its join code
        Flat flat = flatService.findByJoinCode(flatCode);

        if (flat == null) {
            // If no flat is found, add an error message
            model.addAttribute("error", "Invalid flat code. Please try again.");
            return "joinFlat"; // Render the same page with error message
        } else {
            // Pass the flat information to the view
            model.addAttribute("flat", flat);
        }

        // Retrieve the list of all flats (optional, if needed for the grid)
        List<Flat> flats = flatService.getAllFlats();
        model.addAttribute("flats", flats);

        // Set the logged-in user's flat to current flat
        if (user != null) {
            // Update the user's flat association in the database
            appUserService.joinFlat(user, flat);
        } else {
            return "redirect:/login"; // Not logged in? Send them to login.
        }

        return "joinFlat"; // Render the same page with updated information
    }

    @GetMapping("/joinFlatPage")
    public String showJoinFlatPage(Model model) {
        // Retrieve the list of all flats
        List<Flat> flats = flatService.getAllFlats();

        // Add the list of flats to the model
        model.addAttribute("flats", flats);

        return "joinFlat"; // Render the joinFlat.html page
    }
}