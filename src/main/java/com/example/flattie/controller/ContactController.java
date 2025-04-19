package com.example.flattie.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.service.FlatService;
import com.example.flattie.model.MaintenanceTicket;
import com.example.flattie.model.PropertyManager;
import com.example.flattie.repository.FlatRepository;
import com.example.flattie.repository.MaintenanceTicketRepository;
import com.example.flattie.service.EmailService;

/**
 * Controller class for the maintenance ticket system. Maps URLs to html pages.
 * Handles the creation and management of maintenance tickets.
 */
@Controller
public class ContactController {
    @Autowired
    private MaintenanceTicketRepository ticketRepository;
    @Autowired
    private FlatService flatService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FlatRepository flatRepo;

    @GetMapping("/contacts") // Show contacts + ticket list
    public String showContacts(@AuthenticationPrincipal AppUser user, Model model) {
        Flat flat = user.getFlat();
        if (flat == null)
            return "redirect:/joinFlat";
        if (flat.getPropertyManager() == null)
            return "redirect:/propertyManagerForm";

        model.addAttribute("flat", flat);
        model.addAttribute("user", user);
        return "contacts";
    }

    @GetMapping("/propertyManagerForm") // Redirect when no PM is assigned
    public String showPropertyManagerForm(@AuthenticationPrincipal AppUser user, Model model) {
        Flat flat = user.getFlat();
        if (flat == null)
            return "redirect:/joinFlat";

        model.addAttribute("flat", flat);
        return "propertyManagerForm";
    }

    @PostMapping("/propertyManager/save")
    public String savePropertyManagerForm(@AuthenticationPrincipal AppUser user,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String phone) {
        Flat flat = user.getFlat();
        if (flat == null)
            return "redirect:/joinFlat";

        PropertyManager manager = new PropertyManager();
        manager.setName(name);
        manager.setEmail(email);
        manager.setPhone(phone);
        manager.setFlat(flat);

        flat.setPropertyManager(manager);
        flatRepo.save(flat); // cascade saves manager too

        return "redirect:/contacts";
    }
}
