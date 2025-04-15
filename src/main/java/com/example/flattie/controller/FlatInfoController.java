package com.example.flattie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.service.AppUserService;
import com.example.flattie.service.FlatService;

@Controller
public class FlatInfoController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private FlatService flatService;

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

    @PostMapping("/leaveFlat")
    public String leaveFlat(@AuthenticationPrincipal AppUser user, Model model) {
    // Check if the user is logged in
    if (user == null) {
        return "redirect:/login"; // Redirect to login if the user is not authenticated
    }

    // Check if the user belongs to a flat
    Flat flat = user.getFlat();
    if (flat == null) {
        model.addAttribute("error", "You are not part of any flat to leave.");
        return "error"; // Render an error page
    }

    // Remove the user from the flat
    user.setFlat(null);
    appUserService.saveAppUser(user);

    
    model.addAttribute("success", "You have successfully left the flat.");

    // Redirect to a suitable page 
    return "redirect:/";
}

    /**
     * Endpoint to get the flatmates of the currently authenticated user.
     * 
     * @param user The currently authenticated user.
     * @return A list of flatmates associated with the user's flat.
     */
    @GetMapping("/api/flat/flatmates")
    @ResponseBody
    public List<Map<String, Object>> getFlatmates(@AuthenticationPrincipal AppUser user) {
        // This was a workaround to avoid circular reference issues. Trims the data to
        // only include the id and username of each flatmate.
        return flatService.getFlatmates(user.getFlat().getId())
                .stream()
                .map(flatmate -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", flatmate.getId());
                    data.put("username", flatmate.getUsername());
                    return data;
                })
                .toList();
    }

}