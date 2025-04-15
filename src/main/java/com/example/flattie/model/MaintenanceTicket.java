package com.example.flattie.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

@Entity
public class MaintenanceTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String type; // e.g., "PLUMBING", "ELECTRICAL", etc.
    private String urgency; // e.g., "LOW", "MEDIUM", "HIGH"
    private String status; // e.g., "PENDING", "RESOLVED"
    private LocalDateTime submittedAt;
    private String submittedBy;
    private String confirmationToken;
    private String managerEmail; // Email of the manager to whom the ticket is assigned

    @ManyToOne
    private Flat flat;

    public MaintenanceTicket() {
    }

    public MaintenanceTicket(String description, String type, String urgency, String status, LocalDateTime submittedAt,
            String submittedBy, String confirmationToken, Flat flat) {
        this.description = description;
        this.type = type;
        this.urgency = urgency;
        this.status = status;
        this.submittedAt = submittedAt;
        this.submittedBy = submittedBy;
        this.confirmationToken = confirmationToken;
        this.flat = flat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }
}
