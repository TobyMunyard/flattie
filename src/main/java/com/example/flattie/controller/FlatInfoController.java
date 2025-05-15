package com.example.flattie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatMembership;
import com.example.flattie.model.FlatMembershipStatus;
import com.example.flattie.model.Role;
import com.example.flattie.repository.FlatRepository;
import com.example.flattie.service.AppUserService;
import com.example.flattie.service.FlatMembershipService;
import com.example.flattie.service.FlatService;

@Controller
public class FlatInfoController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private FlatService flatService;

    @Autowired
    private FlatMembershipService flatMembershipService;

    @Autowired
    private FlatRepository flatRepo;

    @GetMapping("/showFlatInfo")
    public String showFlatInfo(@AuthenticationPrincipal AppUser user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        if (user.getFlat() == null) {
            return "redirect:/joinFlat";
        }

        Flat flat = flatRepo.findById(user.getFlat().getId())
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        if (flat == null) {
            model.addAttribute("error", "You have not joined a flat.");
            return "error";
        }

        Optional<FlatMembership> membership = flatMembershipService.getMembership(flat, user);

        if (membership.isEmpty() || membership.get().getStatus() != FlatMembershipStatus.APPROVED) {
            return "redirect:/pendingApproval";
        }

        //
        model.addAttribute("membership", membership.orElse(null));
        model.addAttribute("flat", flat);

        return "flatInfo";
    }

    @PostMapping("/updateFlatInfo")
    public String updateFlatInfo(@AuthenticationPrincipal AppUser user,
            @RequestParam("flatId") Long flatId,
            @RequestParam("flatName") String flatName,
            @RequestParam("waiverContents") String waiverContents,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("postcode") String postcode,
            @RequestParam("flatDescription") String flatDescription,
            @RequestParam("weeklyRent") double weeklyRent,
            @RequestParam("rooms") int rooms) {
        // Check if the user is logged in
        if (user == null) {
            return "redirect:/login"; // Redirect to login if the user is not authenticated
        }

        // Retrieve the flat by its ID
        Flat flat = flatRepo.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));

        if (flat == null || !flat.getId().equals(flatId)) {
            return "redirect:/error"; // Redirect to an error page if the flat is not found
        }

        // Update the flat details

        flat.setFlatName(flatName);
        flat.setAddress(address);
        flat.setCity(city);
        flat.setPostcode(postcode);
        flat.setFlatDescription(flatDescription);
        flat.setWeeklyRent(weeklyRent);
        flat.setWaiverContents(waiverContents);
        flat.setRooms(rooms);

        // Save the updated flat
        appUserService.saveFlat(flat);

        // Redirect back to the flat info page
        return "redirect:/showFlatInfo";
    }

    @PostMapping("/leaveFlat")
    public String leaveFlat(@AuthenticationPrincipal AppUser user, Model model) {
        // Check if the user is logged in
        if (user == null) {
            return "redirect:/login"; // Redirect to login if the user is not authenticated
        }

        // Check if the user belongs to a flat
        Flat flat = user.getFlat();
        if (flat == null) {
            model.addAttribute("error", "You are not part of any flat to leave.");
            return "error"; // Render an error page
        }

        // Remove the user from the flat
        user.setFlat(null);
        appUserService.saveAppUser(user);

        model.addAttribute("success", "You have successfully left the flat.");

        // Redirect to a suitable page
        return "redirect:/";
    }

    /**
     * Endpoint to get the flatmates of the currently authenticated user.
     * 
     * @param user The currently authenticated user.
     * @return A list of flatmates associated with the user's flat.
     */
    @GetMapping("/api/flat/flatmates")
    @ResponseBody
    public List<Map<String, Object>> getFlatmates(@AuthenticationPrincipal AppUser user) {
        // This was a workaround to avoid circular reference issues. Trims the data to
        // only include the id and username of each flatmate.
        return flatService.getFlatmates(user.getFlat().getId())
                .stream()
                .map(flatmate -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", flatmate.getId());
                    data.put("username", flatmate.getUsername());
                    data.put("name", flatmate.getFirstName());
                    data.put("bio", flatmate.getBio());
                    data.put("noiseTolerance", flatmate.getNoiseTolerance());
                    data.put("cleanliness", flatmate.getCleanliness());
                    data.put("role", flatMembershipService.getRole(user.getFlat(), flatmate)); // Use role from
                                                                                               // membership
                    return data;
                })
                .toList();
    }

    @PutMapping("/api/flat/{flatId}/members/{userId}/role")
    @ResponseBody
    public ResponseEntity<String> updateRole(@PathVariable Long flatId,
            @PathVariable Long userId,
            @RequestParam("role") String role,
            @AuthenticationPrincipal AppUser adminUser) {
        Flat flat = adminUser.getFlat();

        Optional<FlatMembership> adminMembership = flatMembershipService.getMembership(flat, adminUser);
        if (adminMembership.isEmpty() || adminMembership.get().getRole() != Role.OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only OWNER can change roles.");
        }

        Role newRole = Role.valueOf(role.toUpperCase());
        flatMembershipService.updateRole(flat, userId, newRole);

        return ResponseEntity.ok("Role updated to " + newRole);
    }

    /**
     * Endpoint to show the property manager form.
     * 
     * @param user  The currently authenticated user.
     * @param model The model to be used in the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/propertyManagerForm") // Redirect when no PM is assigned
    public String showPropertyManagerForm(@AuthenticationPrincipal AppUser user, Model model) {
        Flat flat = user.getFlat();
        if (flat == null)
            return "redirect:/joinFlat";

        model.addAttribute("flat", flat);
        return "propertyManagerForm";
    }

    /**
     * Endpoint to save the property manager form data.
     * 
     * @param user  The currently authenticated user.
     * @param name  The name of the property manager.
     * @param email The email of the property manager.
     * @param phone The phone number of the property manager (optional).
     * @return A redirect to the flat info page.
     */
    @PostMapping("/propertyManager/save")
    public String savePropertyManagerForm(@AuthenticationPrincipal AppUser user,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone) {
        Flat flat = flatRepo.findWithPMById(user.getFlat().getId())
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        if (flat == null)
            return "redirect:/joinFlat";

        flatService.assignPropertyManager(flat, name, email, phone);
        return "redirect:/showFlatInfo"; // Redirect to flat info page
    }

    @GetMapping("/flats/{flatId}/pendingRequests")
    public String viewPendingRequests(
            @AuthenticationPrincipal AppUser admin,
            Model model) throws Exception {
        Flat flat = admin.getFlat();
        Optional<FlatMembership> membership = flatMembershipService.getMembership(flat, admin);

        if (membership.isPresent() && membership.get().getRole() == Role.MEMBER) {
            throw new Exception("Only admins or owners can view join requests.");
        }

        List<FlatMembership> pending = flatMembershipService.findPendingByFlat(flat);
        model.addAttribute("flat", flat);
        model.addAttribute("pendingRequests", pending);
        return "pendingRequests";
    }

    @PutMapping("/api/flats/{flatId}/members/{userId}/approve")
    @ResponseBody
    public ResponseEntity<String> approveJoinRequest(@PathVariable("flatId") Long flatId,
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal AppUser adminUser) {

        Flat flat = adminUser.getFlat();

        if (flat == null || !flat.getId().equals(flatId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied to this flat.");
        }

        Optional<FlatMembership> adminMembership = flatMembershipService.getMembership(flat, adminUser);
        if (adminMembership.isEmpty() || adminMembership.get().getRole() == Role.MEMBER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient permissions.");
        }

        AppUser joiningUser = appUserService.getAppUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        FlatMembership request = flatMembershipService
                .getMembership(flat, joiningUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        if (request.getStatus() != FlatMembershipStatus.PENDING) {
            return ResponseEntity.badRequest().body("User is not pending approval.");
        }

        // Approve request and update user
        request.setStatus(FlatMembershipStatus.APPROVED);
        joiningUser.setFlat(flat);
        appUserService.saveAppUser(joiningUser);
        flatMembershipService.save(request);
        // Need to add in here what membership the

        // Refresh session if approving yourself
        if (adminUser.getId().equals(joiningUser.getId())) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    joiningUser,
                    joiningUser.getPassword(),
                    joiningUser.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return ResponseEntity.ok("User approved.");
    }

    @DeleteMapping("/api/flats/{flatId}/members/{userId}/reject")
    @ResponseBody
    public ResponseEntity<String> rejectJoinRequest(@PathVariable("flatId") Long flatId,
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal AppUser adminUser) {

        Flat flat = adminUser.getFlat();

        if (flat == null || !flat.getId().equals(flatId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied to this flat.");
        }

        Optional<FlatMembership> adminMembership = flatMembershipService.getMembership(flat, adminUser);
        if (adminMembership.isEmpty() || adminMembership.get().getRole() == Role.MEMBER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient permissions.");
        }

        Optional<AppUser> joiningUser = appUserService.getAppUserById(userId);
        flatMembershipService.removeUserFromFlat(flat,
                joiningUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));

        return ResponseEntity.ok("User rejected.");
    }

    @GetMapping("/api/flats/{flatId}/pendingRequestsData")
    @ResponseBody
    public Map<String, Object> getPendingRequestsData(@PathVariable("flatId") Long flatId,
            @AuthenticationPrincipal AppUser user) {
        Flat flat = flatService.findById(flatId);
        Optional<FlatMembership> membership = flatMembershipService.getMembership(flat, user);

        if (membership.isEmpty() || membership.get().getRole() == Role.MEMBER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient permissions");
        }

        List<Map<String, Object>> requests = flatMembershipService.findPendingByFlat(flat).stream()
                .map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", m.getUser().getId());
                    map.put("username", m.getUser().getUsername());
                    return map;
                }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("flatName", flat.getFlatName());
        response.put("requests", requests);

        return response;
    }

}