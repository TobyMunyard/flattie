package com.example.flattie.repository;

import org.springframework.stereotype.Repository;

import com.example.flattie.model.ChoreList;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with ChoreList entities within
 * the database. Offers several built in methods that are called in
 * ChoreListService.
 */
@Repository
public interface ChoreListRepository extends JpaRepository<ChoreList, Long> {
    // Method to find the chore list by flat ID.
    ChoreList findByFlatId(Long flatId);
}
