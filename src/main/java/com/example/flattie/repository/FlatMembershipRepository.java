package com.example.flattie.repository;

import com.example.flattie.model.FlatMembership;
import com.example.flattie.model.FlatMembershipStatus;
import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlatMembershipRepository extends JpaRepository<FlatMembership, Long> {
    Optional<FlatMembership> findByFlatAndUser(Flat flat, AppUser user);
    List<FlatMembership> findAllByFlat(Flat flat);
    void deleteByFlatAndUser(Flat flat, AppUser user);
    List<FlatMembership> findByFlatAndStatus(Flat flat, FlatMembershipStatus status);
}



