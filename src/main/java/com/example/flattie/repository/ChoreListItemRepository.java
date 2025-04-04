package com.example.flattie.repository;

import org.springframework.stereotype.Repository;
import com.example.flattie.model.ChoreListItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with ChoreListItem entities within
 * the database. Offers several built in methods that are called in
 * ChoreListItemService.
 */
@Repository
public interface ChoreListItemRepository extends JpaRepository<ChoreListItem, Long> {

    // Method to find list of chores by choreList ID.
    List<ChoreListItem> findByChoreList_Id(Long choreListId);

    // Method to find a chore by its name.
    List<ChoreListItem> findByChoreNameContainingIgnoreCase(String choreName);
}
