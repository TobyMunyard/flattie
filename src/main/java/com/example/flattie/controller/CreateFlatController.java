package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.flattie.model.Flat;
import com.example.flattie.service.FlatService;

/**
 * Controller class for handling all requests based on flat creation
 * within the system.
 */

@Controller
public class CreateFlatController {

    @Autowired
    FlatService flatService;

    /**
     * Displays the create flat page.
     * 
     * @return The create flat HTML page.
     */
    @GetMapping("/createFlat")
    public String showCreateFlatPage() {
        return "createFlat"; // This should match the name of your HTML file (without .html)
    }

    /**
     * Called upon the submission of the flat creation form. Checks that input
     * values are valid.
     * 
     * @param flatName        The name of the flat.
     * @param address         The address of the flat.
     * @param city            The city where the flat is located.
     * @param postcode        The postcode of the flat.
     * @param flatDescription A description of the flat.
     * @return If details are valid, redirect to a success page or dashboard.
     *         Otherwise, redirect back to the flat creation page with the error displayed.
     */
    @PostMapping("/createFlat")
public String createFlat(@RequestParam("flatName") String flatName,
                         @RequestParam("address") String address,
                         @RequestParam("city") String city,
                         @RequestParam("postcode") String postcode,
                         @RequestParam("flatDescription") String flatDescription,
                         @RequestParam("weeklyRent") double weeklyRent,
                         @RequestParam("rooms") int rooms,
                         Model model,
                         RedirectAttributes redirectAttributes) {

    // In case no errors occur this will satisfy Thymeleaf
    model.addAttribute("error", null);

    // Validation for flat inputs
    if (flatName == null || flatName.isBlank() || flatName.length() > 50) {
        redirectAttributes.addFlashAttribute("error", "Flat name must be under 50 characters.");
        return "redirect:/createFlat";
    }

    if (address == null || address.isBlank() || address.length() > 100) {
        redirectAttributes.addFlashAttribute("error", "Address must be under 100 characters.");
        return "redirect:/createFlat";
    }

    // Check if the address already exists
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

    // Generate a random join code
    String joinCode = generateRandomCode();

    // All inputs are valid, create the flat
    Flat newFlat = new Flat(joinCode, flatName, address, city, postcode, flatDescription, weeklyRent, rooms);
    flatService.saveFlat(newFlat);

    return "redirect:/viewFlats"; // Redirect to a dashboard or success page
}

// Method to generate a random alphanumeric code
private String generateRandomCode() {
    return java.util.UUID.randomUUID().toString().substring(0, 5); // Generate an 5-character random code
}
    
    }