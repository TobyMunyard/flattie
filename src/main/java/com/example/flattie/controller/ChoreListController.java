package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/choreList")
    public String getChoreList(HttpSession session, Model model) {
        AppUser currentUser = (AppUser) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login"; // Not logged in? Send them to login.
        }

        // Get the flat from the user
        Flat userFlat = currentUser.getFlat();
        if (userFlat != null) {
            // Fetch chores for the flat using a service method
            List<ChoreListItem> chores = choreService.getChoresForFlat(userFlat.getId());
            model.addAttribute("chores", chores);
        }

        // Pass the user info (if needed idk)
        model.addAttribute("currentUser", currentUser);
        return "choreList"; // Thymeleaf page
    }
}
