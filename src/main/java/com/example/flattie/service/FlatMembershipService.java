package com.example.flattie.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.flattie.model.FlatMembership;
import com.example.flattie.model.FlatMembershipStatus;
import com.example.flattie.model.Flat;
import com.example.flattie.model.AppUser;
import com.example.flattie.model.Role;

import com.example.flattie.repository.FlatMembershipRepository;

@Service
public class FlatMembershipService {

    @Autowired
    private FlatMembershipRepository repository;

    public List<FlatMembership> getMembersOfFlat(Flat flat) {
        return repository.findAllByFlat(flat);
    }

    public void updateRole(Flat flat, AppUser user, Role newRole) {
        FlatMembership m = repository.findByFlatAndUser(flat, user)
                .orElseThrow(() -> new RuntimeException("User not in flat"));
        m.setRole(newRole);
        repository.save(m);
    }

    

    public Optional<FlatMembership> getMembership(Flat flat, AppUser user) {
        return repository.findByFlatAndUser(flat, user);
    }

    public Role getRole(Flat flat, AppUser user) {
        return getMembership(flat, user)
                .map(FlatMembership::getRole)
                .orElse(Role.MEMBER); // or throw if you want stricter handling
    }

    public void removeUserFromFlat(Flat flat, AppUser user) {
        repository.deleteByFlatAndUser(flat, user);
    }

    public void save(FlatMembership membership) {
        repository.save(membership);
    }

    public List<FlatMembership> findPendingByFlat(Flat flat) {
        return repository.findByFlatAndStatus(flat, FlatMembershipStatus.PENDING);
    }

    public void updateRole(Flat flat, Long userId, Role newRole) {
        FlatMembership membership = repository.findByFlatIdAndUserId(flat.getId(), userId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        membership.setRole(newRole);
        repository.save(membership);
    }

}
