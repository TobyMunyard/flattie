package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatMembership;
import com.example.flattie.model.FlatMembershipStatus;
import com.example.flattie.model.Role;
import com.example.flattie.service.AppUserService;
import com.example.flattie.service.FlatMembershipService;
import com.example.flattie.service.FlatService;

import jakarta.annotation.PostConstruct;

@Controller
public class CreateFlatController {

    @Autowired
    FlatService flatService;

    @Autowired
    FlatMembershipService flatMembershipService;

    @Autowired
    AppUserService appUserService;

    @GetMapping("/createFlat")
    public String showCreateFlatPage() {
        return "createFlat";
    }

    @PostMapping("/createFlat")
    public String createFlat(
            @AuthenticationPrincipal AppUser user,
            @RequestParam("flatName") String flatName,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("postcode") String postcode,
            @RequestParam("flatDescription") String flatDescription,
            @RequestParam("weeklyRent") double weeklyRent,
            @RequestParam("rooms") int rooms,
            Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("error", null);

        if (flatName == null || flatName.isBlank() || flatName.length() > 50) {
            redirectAttributes.addFlashAttribute("error", "Flat name must be under 50 characters.");
            return "redirect:/createFlat";
        }

        if (address == null || address.isBlank() || address.length() > 100) {
            redirectAttributes.addFlashAttribute("error", "Address must be under 100 characters.");
            return "redirect:/createFlat";
        }

        if (flatService.addressExists(address)) {
            redirectAttributes.addFlashAttribute("error", "A flat with this address already exists.");
            return "redirect:/createFlat";
        }

        if (city == null || city.isBlank() || city.length() > 50) {
            redirectAttributes.addFlashAttribute("error", "City must be under 50 characters.");
            return "redirect:/createFlat";
        }

        if (postcode == null || postcode.isBlank() || postcode.length() > 10) {
            redirectAttributes.addFlashAttribute("error", "Postcode must be under 10 characters.");
            return "redirect:/createFlat";
        }

        if (flatDescription == null || flatDescription.isBlank() || flatDescription.length() > 500) {
            redirectAttributes.addFlashAttribute("error", "Description must be under 500 characters.");
            return "redirect:/createFlat";
        }

        if (weeklyRent <= 0) {
            redirectAttributes.addFlashAttribute("error", "Weekly rent must be a positive value.");
            return "redirect:/createFlat";
        }

        if (rooms <= 0) {
            redirectAttributes.addFlashAttribute("error", "Number of rooms must be a positive integer.");
            return "redirect:/createFlat";
        }

        String joinCode = generateRandomCode();
        Flat newFlat = new Flat(joinCode, flatName, address, city, postcode, flatDescription, weeklyRent, rooms);
        flatService.saveFlat(newFlat);

        if (user.getFlat() == null) {
            user.setFlat(newFlat);
            appUserService.saveAppUser(user);

            FlatMembership membership = new FlatMembership();
            membership.setFlat(newFlat);
            membership.setUser(user);
            membership.setRole(Role.OWNER);
            membership.setStatus(FlatMembershipStatus.APPROVED);

            flatMembershipService.save(membership);

            System.out.println("Flat created with join code: " + joinCode);
        }

        redirectAttributes.addFlashAttribute("success", "Flat created and joined successfully!");
        return "redirect:/showFlatInfo";
    }

    private String generateRandomCode() {
        return java.util.UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    @PostConstruct
    public void createDefaultFlats() {
        Flat existingFlat1 = flatService.findByJoinCode("123 Default St");
        Flat existingFlat2 = flatService.findByJoinCode("456 Default St");

        if (existingFlat1 == null) {
            Flat defaultFlat1 = new Flat("1234", "Default Flat 1", "123 Default St", "Default City", "0000", "A default flat for new users.", 200.0, 3);
            flatService.saveFlat(defaultFlat1);
            System.out.println("Default flat created at 123 Default St");
        }

        if (existingFlat2 == null) {
            Flat defaultFlat2 = new Flat("5678", "Default Flat 2", "456 Default St", "Default City", "0000", "A second default flat for newer, cooler, users.", 200.0, 3);
            flatService.saveFlat(defaultFlat2);
            System.out.println("Default flat created at 456 Default St");
        }
    }
}
