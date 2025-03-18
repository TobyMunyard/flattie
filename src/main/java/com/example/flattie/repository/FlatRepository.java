package com.example.flattie.repository;

import org.springframework.stereotype.Repository;
import com.example.flattie.model.Flat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with Flat entities within
 * the database. Offers several built in methods that are called in
 * FlatService. Future custom queries will be added here and then
 * called with the service class.
 */
@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {

    // Empty right now but can be populated with custom queries at a later date.
    
}