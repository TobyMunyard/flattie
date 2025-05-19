package com.example.flattie.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.flattie.model.ChoreListItem;
import com.example.flattie.repository.ChoreListItemRepository;

/**
 * Service class for database interaction with ChoreListItem entities.
 * Performs actual calling of ChoreListItemRepository methods.
 */
@Service
public class ChoreListItemService {
    private final ChoreListItemRepository choreListItemRepository;

    @Autowired
    public ChoreListItemService(ChoreListItemRepository choreListItemRepository) {
        this.choreListItemRepository = choreListItemRepository;
    }

    /**
     * Saves a new ChoreListItem to the database.
     * 
     * @param choreListItem The ChoreListItem to persist in the database.
     * @return The saved entity.
     */
    public ChoreListItem saveItem(ChoreListItem choreListItem) {
        return choreListItemRepository.save(choreListItem);
    }

    /**
     * Finds a list of chores by chore list ID.
     * 
     * @param flatId The ID of the chore list to find chores for.
     * @return A list of ChoreListItem entities associated with the given chore list
     *         ID.
     */
    public List<ChoreListItem> findByListId(Long choreListId) {
        return choreListItemRepository.findByChoreList_Id(choreListId);
    }
}
