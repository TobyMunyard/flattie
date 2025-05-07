package com.example.flattie.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.FlatMembershipService;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatMembership;
import com.example.flattie.model.Role;
import com.example.flattie.model.FlatMembershipStatus; // Ensure this import exists
import com.example.flattie.service.FlatService;

@Controller
public class JoinFlatController {

    @Autowired
    private FlatService flatService;

    @Autowired
    private FlatMembershipService flatMembershipService;

    @PostMapping("/joinFlat")
    public String joinFlat(@AuthenticationPrincipal AppUser user,
            @RequestParam("flat_code") String flatCode,
            Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        Flat flat = flatService.findByJoinCode(flatCode);
        if (flat == null) {
            model.addAttribute("error", "Invalid flat code. Please try again.");
            return "joinFlat";
        }

        Optional<FlatMembership> existing = flatMembershipService.getMembership(flat, user);
        if (existing.isPresent()) {
            model.addAttribute("error", "You're already a member of this flat.");
            return "joinFlat";
        }

        // Create and save membership
        FlatMembership membership = new FlatMembership();
        membership.setFlat(flat);
        membership.setUser(user);
        membership.setRole(Role.MEMBER);
        membership.setStatus(FlatMembershipStatus.PENDING); // not approved yet
        flatMembershipService.save(membership);

        model.addAttribute("message", "Join request sent. Please wait for approval.");
        return "redirect:/pendingApproval";
    }

    @GetMapping("/pendingApproval")
    public String showPendingApprovalPage() {
        return "pendingApproval"; // tells Spring to render templates/pendingApproval.html
    }

}