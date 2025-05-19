package com.example.flattie.repository;

import org.springframework.stereotype.Repository;
import com.example.flattie.model.ChoreListItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT c FROM ChoreListItem c WHERE c.choreList.flat.id = :flatId AND LOWER(c.choreName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ChoreListItem> searchByFlatAndQuery(@Param("flatId") Long flatId, @Param("query") String query);

}
