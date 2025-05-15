package com.example.flattie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatMembership;
import com.example.flattie.model.FlatMembershipStatus;
import com.example.flattie.model.Role;
import com.example.flattie.service.AppUserService;
import com.example.flattie.service.FlatMembershipService;
import com.example.flattie.service.FlatService;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private FlatService flatService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private FlatMembershipService flatMembershipService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Step 1: Create Default Flats
        Flat flat1 = flatService.findByJoinCode("1234");
        if (flat1 == null) {
            flat1 = new Flat("1234", "Default Flat 1", "123 Default St", "Default City", "0000",
                    "A default flat for new users.", 200.0, 3);
            flatService.saveFlat(flat1);
            System.out.println("Created Default Flat 1");
        }

        Flat flat2 = flatService.findByJoinCode("5678");
        if (flat2 == null) {
            flat2 = new Flat("5678", "Default Flat 2", "456 Default St", "Default City", "0000",
                    "A second default flat for newer, cooler, users.", 200.0, 3);
            flatService.saveFlat(flat2);
            System.out.println("Created Default Flat 2");
        }

        // Step 2: Create Default Users
        AppUser tester = appUserService.getAppUserByUsername("Tester").orElse(null);
        if (tester == null) {
            tester = new AppUser("Test", "User", "Tester", passwordEncoder.encode("test1234"));
            tester.setFlat(flat1);
            appUserService.saveAppUser(tester);
            System.out.println("Created Tester");
        }

        AppUser jane = appUserService.getAppUserByUsername("JaneDoe").orElse(null);
        if (jane == null) {
            jane = new AppUser("Jane", "Doe", "JaneDoe", passwordEncoder.encode("pass123"));
            appUserService.saveAppUser(jane);
            System.out.println("Created JaneDoe");
        }

        // Step 3: Assign Membership (only if not already assigned)
        if (flatMembershipService.getMembership(flat1, tester).isEmpty()) {
            FlatMembership membership = new FlatMembership();
            membership.setFlat(flat1);
            membership.setUser(tester);
            membership.setRole(Role.OWNER);
            membership.setStatus(FlatMembershipStatus.APPROVED);
            flatMembershipService.save(membership);
            System.out.println("Assigned Tester as OWNER");
        }
    }
}


