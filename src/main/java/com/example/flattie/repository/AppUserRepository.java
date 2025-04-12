package com.example.flattie.repository;

import org.springframework.stereotype.Repository;
import com.example.flattie.model.AppUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with AppUser entities within
 * the database. Offers several built in methods that are called in
 * AppUserService. Future custom queries will be added here and then
 * called with the service class.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    public Optional<AppUser> getAppUserByUsername(String username); // Custom query method to find a user by username
    List<AppUser> findByFlat_Id(Long flatId); // Custom query method to find users by flat ID
}