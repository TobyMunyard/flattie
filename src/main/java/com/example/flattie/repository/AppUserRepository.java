package com.example.flattie.repository;

import org.springframework.stereotype.Repository;
import com.example.flattie.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with AppUser entities within
 * the database. Offers several built in methods that are called in
 * AppUserService. Future custom queries will be added here and then
 * called with the service class.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // Empty right now but can be populated with custom queries at a later date.
}