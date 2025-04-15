package com.example.flattie.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<Flat> flats = flatService.getAllFlats();
        model.addAttribute("flats", flats);
        model.addAttribute("user", user);
        return "contacts";
    }

    @PostMapping("/maintenance/request")
    public String requestMaintenance(@AuthenticationPrincipal AppUser user,
            @RequestParam String description,
            @RequestParam String managerEmail,
            RedirectAttributes redirectAttributes) {
        MaintenanceTicket ticket = new MaintenanceTicket();
        ticket.setDescription(description);
        ticket.setFlat(user.getFlat());
        ticket.setStatus("PENDING");
        ticket.setConfirmationToken(UUID.randomUUID().toString());
        ticket.setSubmittedBy(user.getUsername());
        ticket.setSubmittedAt(LocalDateTime.now());

        ticketRepository.save(ticket);

        String confirmLink = "http://localhost:8080/maintenance/confirm/" + ticket.getConfirmationToken();

        emailService.sendMaintenanceEmail(
                managerEmail,
                "Maintenance Request for " + user.getFlat().getFlatName(),
                "Issue: " + description + "\n\nMark as resolved: " + confirmLink);

        redirectAttributes.addFlashAttribute("success", "Ticket submitted and email sent!");
        return "redirect:/maintenanceList";
    }

    @GetMapping("/propertyManagerForm") // Redirect when no PM is assigned
    @PostMapping("/propertyManager/save")
    public String savePropertyManager(@AuthenticationPrincipal AppUser user,
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
