package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.service.AppUserService;

@Controller
public class FlatInfoController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/showFlatInfo")
    public String showFlatInfo(@AuthenticationPrincipal AppUser user, Model model) {
        // Check if the user is logged in
        if (user == null) {
            return "redirect:/login"; // Redirect to login if the user is not authenticated
        }

        // Retrieve the flat associated with the user
        Flat flat = user.getFlat();

        if (flat == null) {
            // If the user has not joined any flat, show an error message
            model.addAttribute("error", "You have not joined a flat.");
            return "error"; // Render an error page
        }

        // Add the flat details to the model
        model.addAttribute("flat", flat);

        // Render the flatInfo.html page
        return "flatInfo";
    }
    
    @PostMapping("/updateFlatInfo")
    public String updateFlatInfo(@AuthenticationPrincipal AppUser user,
                                 @RequestParam("flatId") Long flatId,
                                 @RequestParam("flatName") String flatName,
                                 @RequestParam("address") String address,
                                 @RequestParam("city") String city,
                                 @RequestParam("postcode") String postcode,
                                 @RequestParam("flatDescription") String flatDescription,
                                 @RequestParam("weeklyRent") double weeklyRent,
                                 @RequestParam("rooms") int rooms) {
        // Check if the user is logged in
        if (user == null) {
            return "redirect:/login"; // Redirect to login if the user is not authenticated
        }
    
        // Retrieve the flat by its ID
        Flat flat = user.getFlat();
    
        if (flat == null || !flat.getId().equals(flatId)) {
            return "redirect:/error"; // Redirect to an error page if the flat is not found
        }
    
        // Update the flat details
        flat.setFlatName(flatName);
        flat.setAddress(address);
        flat.setCity(city);
        flat.setPostcode(postcode);
        flat.setFlatDescription(flatDescription);
        flat.setWeeklyRent(weeklyRent);
        flat.setRooms(rooms);
    
        // Save the updated flat
        appUserService.saveFlat(flat);
    
        // Redirect back to the flat info page
        return "redirect:/showFlatInfo";
    }
}