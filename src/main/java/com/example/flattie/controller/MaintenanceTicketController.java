package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.MaintenanceTicket;
import com.example.flattie.service.EmailService;
import com.example.flattie.service.MaintenanceTicketService;

@Controller
public class MaintenanceTicketController {

    @Autowired
    private MaintenanceTicketService ticketService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/ticket")
    public String ticketPage(@AuthenticationPrincipal AppUser user, Model model) {
        Flat flat = user.getFlat();
        List<MaintenanceTicket> tickets = ticketService.getTicketsForFlat(flat);
        model.addAttribute("tickets", tickets);
        return "ticket";
    }

    @PostMapping("/maintenance/request")
    public String submitTicket(@AuthenticationPrincipal AppUser user,
            @RequestParam String description,
            @RequestParam String urgency,
            RedirectAttributes redirectAttributes) {

        MaintenanceTicket ticket = ticketService.createTicket(user, description, urgency);

        String confirmLink = "http://localhost:8080/maintenance/confirm/" + ticket.getConfirmationToken();
        String body = "A new maintenance issue has been submitted:\n\n"
                + ticket.getDescription() + "\n\n"
                + "Click to mark as resolved: " + confirmLink;

        emailService.sendMaintenanceEmail(ticket.getManagerEmail(),
                "Maintenance Request - " + user.getFlat().getFlatName(),
                body);

        redirectAttributes.addFlashAttribute("success", "Ticket submitted and email sent!");
        return "redirect:/ticket";
    }

    @GetMapping("/maintenance/confirm/{token}")
    @ResponseBody
    public String confirmTicket(@PathVariable String token) {
        boolean success = ticketService.resolveTicket(token);
        return success ? "Ticket marked as resolved." : "Invalid or expired confirmation link.";
    }
}
