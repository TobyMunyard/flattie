package com.example.flattie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.flattie.model.Flat;
import com.example.flattie.model.MaintenanceTicket;

/**
 * Repository interface for interacting with Flat entities within
 * the database. Offers several built in methods that are called in
 * FlatService. Future custom queries will be added here and then
 * called with the service class.
 */
@Repository
public interface MaintenanceTicketRepository extends JpaRepository<MaintenanceTicket, Long> {
    MaintenanceTicket findByConfirmationToken(String confirmationToken); // Custom query method to find a maintenance by confirmation token

    List<MaintenanceTicket> findByFlat(Flat flat);
}