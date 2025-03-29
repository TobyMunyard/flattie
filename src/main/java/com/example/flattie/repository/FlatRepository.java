package com.example.flattie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.flattie.model.Flat;

/**
 * Repository interface for interacting with Flat entities within
 * the database. Offers several built in methods that are called in
 * FlatService. Future custom queries will be added here and then
 * called with the service class.
 */
@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {
    boolean existsByAddress(String address); // Custom query method to check if an address exists

    // Empty right now but can be populated with custom queries at a later date.
}