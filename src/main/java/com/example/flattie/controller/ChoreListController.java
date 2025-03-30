package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.ChoreListItem;
import com.example.flattie.model.Flat;
import com.example.flattie.repository.AppUserRepository;
import com.example.flattie.repository.ChoreListItemRepository;
import com.example.flattie.service.ChoreListService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChoreListController {
    @Autowired
    private AppUserRepository userRepository;
    private ChoreListItemRepository choreListItemRepository;
    private ChoreListService choreService;

    /**
     * Serves the chore list page of the application from the url "/choreList".
     * "choreList" string is automatically mapped to file "choreList.html" in
     * resources/templates folder.
     * 
     * @return The choreList page of the application.
     */
    @GetMapping("/choreList")
    public String getChoreList(HttpSession session, @AuthenticationPrincipal AppUser user, Model model) {

        // Get the flat from the user
        Flat userFlat = user.getFlat();
        if (userFlat != null) {
            // Fetch chores for the flat using a service method
            List<ChoreListItem> chores = choreService.getChoresForFlat(userFlat.getId());
            model.addAttribute("chores", chores);
        }

        // Pass the user info (if needed idk)
        model.addAttribute("currentUser", user);
        return "choreList"; // Thymeleaf page
    }
}
