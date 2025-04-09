package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @ResponseBody
    public ResponseEntity<ChoreListItem> addChore(@ModelAttribute ChoreListItem choreListItem,
            @AuthenticationPrincipal AppUser user) {
        if (user == null || user.getFlat() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Flat userFlat = user.getFlat();
        ChoreList choreList = choreListService.getChoreListForFlat(userFlat.getId());

        if (choreList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        choreList.addChore(choreListItem);
        choreListService.saveChoreList(choreList);

        return ResponseEntity.ok(choreListItem);
    }

    /**
     * Handles editing the chore item. The chore is updated in the chore list for
     * the currently authenticated user's flat.
     * 
     * @param id            The ID of the chore item to be edited
     * @param choreListItem The updated chore item data
     * @param user          The currently authenticated user
     * @return A response entity indicating the result of the operation
     */
    @PostMapping("/chore/edit/{id}")
    public ResponseEntity<?> editChore(@PathVariable Long id,
            @ModelAttribute ChoreListItem choreListItem,
            @AuthenticationPrincipal AppUser user) {
        // Validate the user and flat
        if (user == null || user.getFlat() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized or flat not found");
        }

        // Update the chore item in the database
        choreListService.updateChore(id, choreListItem);
        return ResponseEntity.ok(choreListItem); // return updated data
    }

    /**
     * Handles marking a chore as completed and not. The chore is toggled completed
     * in the
     * chore list for the currently authenticated user's flat.
     * 
     * @param id    The ID of the chore item to be marked as completed
     * @param user  The currently authenticated user
     * @param model The model to be populated with the updated chore list
     * @return The updated chore list page
     */
    @PostMapping("/chore/toggleComplete/{id}")
    public String toggleChoreComplete(@PathVariable Long id, @AuthenticationPrincipal AppUser user, Model model) {
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

        // Mark the chore as completed
        choreListService.toggleChoreCompletion(id);

        // Fetch updated list of chore items after toggle
        List<ChoreListItem> updatedChores = choreListService.getChoreListItems(choreList.getId());
        model.addAttribute("chores", updatedChores);

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
