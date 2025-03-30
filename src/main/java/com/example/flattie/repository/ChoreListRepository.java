package com.example.flattie.repository;

import org.springframework.stereotype.Repository;

import com.example.flattie.model.ChoreList;
import com.example.flattie.model.ChoreListItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for interacting with ChoreList entities within
 * the database. Offers several built in methods that are called in
 * ChoreListService. Future custom queries will be added here and then
 * called with the service class.
 */
@Repository
public interface ChoreListRepository extends JpaRepository<ChoreList, Long> {
    List<ChoreListItem> findByFlatId(Long flatId);
}
