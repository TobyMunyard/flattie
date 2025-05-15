package com.example.flattie.controller;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.flattie.model.Flat;
import com.example.flattie.model.AppUser;
import com.example.flattie.service.EmailService;
import com.example.flattie.service.ImageService;
import com.example.flattie.model.MaintenanceTicket;
import com.example.flattie.service.MaintenanceTicketService;
import com.example.flattie.repository.FlatRepository;

@Controller
public class MaintenanceTicketController {

    @Autowired
    private MaintenanceTicketService ticketService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FlatRepository flatRepository;

    @GetMapping("/ticket")
    public String ticketPage(@AuthenticationPrincipal AppUser user, Model model) {
        // Check if user is logged in and has a flat assigned
        if (user == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }

        // Check if the user has a flat assigned
        if (user.getFlat() == null) {
            return "redirect:/joinFlat"; // Redirect to login if no flat assigned
        }

        // Get the user's flat and tickets
        Flat flat = flatRepository.findById(user.getFlat().getId())
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        List<MaintenanceTicket> tickets = ticketService.getTicketsForFlat(flat);

        model.addAttribute("flat", flat);
        model.addAttribute("tickets", tickets);

        return "ticket";
    }

    @PostMapping("/maintenance/request")
    public String submitTicket(@AuthenticationPrincipal AppUser user,
            @RequestParam String description,
            @RequestParam String urgency,
            @RequestParam String location,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {

        // 1. Create ticket with all fields
        MaintenanceTicket ticket = ticketService.createAndSaveTicket(user, description, urgency, location, "PLUMBING");

        try {
            String imagePath = imageService.saveImage(image, "tickets");
            ticket.setImageUrl(imagePath);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Image upload failed");
            return "redirect:/ticket";
        }
        
        // 2. Prepare email body AFTER everything is set
        String confirmLink = "http://localhost:8080/maintenance/confirm/" + ticket.getConfirmationToken();

        // 3. Format the date and time for the email
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' HH:mm");
        String formattedTime = ticket.getSubmittedAt().format(formatter);

        // Build email body
        StringBuilder body = new StringBuilder();
        body.append("A new maintenance request has been submitted.\n\n")
                .append("Dear ").append(user.getFlat().getPropertyManager().getName()).append(",\n\n")
                .append("A new maintenance request has been submitted by ").append(user.getUsername()).append(".\n\n")
                .append("Flat: ").append(user.getFlat().getFlatName()).append("\n")
                .append("Submitted at: ").append(formattedTime).append("\n")
                .append("Description: ").append(ticket.getDescription()).append("\n")
                .append("Location: ").append(ticket.getLocation()).append("\n")
                .append("Urgency: ").append(ticket.getUrgency()).append("\n\n")
                .append("Click here to mark this issue as resolved: ").append(confirmLink);

        // Try to attach image (if it exists)
        File imageFile = null;
        if (ticket.getImageUrl() != null) {
            // Remove leading slash from path
            String relativePath = ticket.getImageUrl().replaceFirst("^/", "");
            imageFile = new File(System.getProperty("user.dir"), relativePath);
        }

        // Send email to property manager
        emailService.sendMaintenanceEmail(
                ticket.getManagerEmail(),
                "Maintenance Request - " + user.getFlat().getFlatName(),
                body.toString(),
                imageFile);

        redirectAttributes.addFlashAttribute("success", "Ticket submitted and email sent!");
        return "redirect:/ticket";
    }

    @GetMapping("/maintenance/confirm/{token}")
    @ResponseBody
    public String confirmTicket(@PathVariable("token") String token) {
        boolean success = ticketService.resolveTicket(token);
        return success ? "Ticket marked as resolved." : "Invalid or expired confirmation link.";
    }
}
