package com.example.flattie.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.MaintenanceTicket;
import com.example.flattie.repository.MaintenanceTicketRepository;

@Service
public class MaintenanceTicketService {

    @Autowired
    private MaintenanceTicketRepository ticketRepo;

    @Autowired
    private FlatService flatService;

    public List<MaintenanceTicket> getTicketsForFlat(Flat flat) {
        return ticketRepo.findByFlat(flat);
    }

    public MaintenanceTicket createAndSaveTicket(AppUser user, String description, String urgency, String location,
            String type) {
        // Create a new MaintenanceTicket object
        if (user == null || user.getFlat() == null) {
            throw new IllegalArgumentException("User or flat cannot be null");
        }

        // Fully fetch Flat with PropertyManager
        Flat flat = flatService.getFlatWithPM(user.getFlat().getId());

        MaintenanceTicket ticket = new MaintenanceTicket();
        ticket.setFlat(flat);
        ticket.setDescription(description);
        ticket.setUrgency(urgency.toUpperCase());
        ticket.setStatus("PENDING");
        ticket.setSubmittedBy(user.getUsername());
        ticket.setSubmittedAt(LocalDateTime.now());
        ticket.setConfirmationToken(UUID.randomUUID().toString());
        ticket.setManagerEmail(flat.getPropertyManager().getEmail());
        ticket.setLocation(location);
        ticket.setType(type);

        // Save the ticket to the database
        ticketRepo.save(ticket);
        return ticket;
    }

    public boolean resolveTicket(String token) {
        MaintenanceTicket ticket = ticketRepo.findByConfirmationToken(token);
        if (ticket != null) {
            ticket.setStatus("RESOLVED");
            ticketRepo.save(ticket);
            return true;
        }
        return false;
    }
}
