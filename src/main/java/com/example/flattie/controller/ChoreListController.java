package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.ChoreListItem;
import com.example.flattie.model.ChoreList;
import com.example.flattie.model.Flat;
import com.example.flattie.service.ChoreListService;

/**
 * Controller class for handling requests related to the chore list page.
 * This class is responsible for serving the chore list page and managing
 * the interaction between the view and the service layer.
 */
@Controller
public class ChoreListController {

    @Autowired
    private ChoreListService choreListService;

    /**
     * Serves the chore list page of the application from the url "/choreList".
     * "choreList" string is automatically mapped to file "choreList.html" in
     * resources/templates folder.
     * 
     * @return The choreList page of the application.
     */
    @GetMapping("/choreList")
    public String getChoreList(@AuthenticationPrincipal AppUser user, Model model) {
        if (user == null) {
            model.addAttribute("error", "You are not logged in.");
            return "redirect:/login"; // Redirect to login page if user is not logged in
        }

        // Get the flat from the user
        Flat userFlat = user.getFlat();
        if (userFlat == null) {
            model.addAttribute("error", "You are not assigned to a flat.");
            return "redirect:/joinFlat"; // Redirect to join flat page if no flat is assigned
        }

        // Fetch chores for the flat using a service method
        ChoreList choreList = choreListService.getChoreListForFlat(userFlat.getId());
        if (choreList == null) {
            // This should never happen as flats are constructed with a chore list, but just
            // in case...
            model.addAttribute("error", "Chore list not found for your flat.");
            return "errorPage";
        }
        List<ChoreListItem> chores = choreList.getChoreListItems();

        // Add the chores to the model for rendering in the view
        model.addAttribute("chores", chores);

        // Pass the user information to the model
        // This is the current user, which is used in the Thymeleaf template
        // model.addAttribute("currentUser", user);
        return "choreList"; // Thymeleaf page
    }

    /**
     * Handles adding a new chore item to the list. The chore is added to the chore
     * list
     * for the currently authenticated user's flat.
     * 
     * @param choreListItem The new chore item to be added
     * @param user          The currently authenticated user
     * @param model         The model to be populated with the updated chore list
     * @return The updated chore list page
     */
    @PostMapping("/choreList/add")
    public String addChore(@ModelAttribute ChoreListItem choreListItem, @AuthenticationPrincipal AppUser user,
            Model model) {
        if (user == null) {
            model.addAttribute("error", "You are not logged in.");
            return "redirect:/login"; // Redirect to login page if user is not logged in
        }

        // Get the flat from the user
        Flat userFlat = user.getFlat();
        if (userFlat == null) {
            model.addAttribute("error", "You are not assigned to a flat.");
            return "redirect:/joinFlat"; // Redirect to join flat page if no flat is assigned
        }

        // Fetch the current chore list for the flat
        ChoreList choreList = choreListService.getChoreListForFlat(userFlat.getId());
        if (choreList == null) {
            model.addAttribute("error", "Chore list not found for your flat.");
            return "errorPage";
        }

        // Add the new chore item to the chore list
        choreList.addChore(choreListItem);
        choreListService.saveChoreList(choreList); // Save the updated chore list

        // Add updated list to model for rendering
        model.addAttribute("chores", choreList.getChoreListItems());

        // Clear the form for next input
        // model.addAttribute("choreListItem", new ChoreListItem());

        return "choreList"; // Return the same page to show updated chore list
    }

    @DeleteMapping("/chore/delete/{id}")
    public ResponseEntity<Void> deleteChore(@PathVariable Long id) {
        choreListService.deleteChoreFromFlat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chore/search")
    @ResponseBody
    public List<ChoreListItem> searchChores(@RequestParam String query) {
        // Logic to search chores
        return choreListService.searchChoreListItemsByName(query);
    }
}
