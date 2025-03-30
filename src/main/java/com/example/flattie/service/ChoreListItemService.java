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
     * Gets all chore list items currently in the database.
     * @return A list of all current ChoreListItem entities in the database.
     */
    public List<ChoreListItem> getAllItems() {
        return choreListItemRepository.findAll();
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
     * Deletes a ChoreListItem from the database based on a provided id.
     * 
     * @param id The id of the ChoreListItem to delete from the database.
     */
    public void deleteItem(Long id) {   
        choreListItemRepository.deleteById(id);
    }


    /**
     * Finds a list of chores by flat ID.
     * 
     * @param flatId The ID of the flat to find chores for.
     * @return A list of ChoreListItem entities associated with the given flat ID.
     */
    // public List<ChoreListItem> findByFlatId(Long flatId) {
    //     return choreListItemRepository.findByFlatId(flatId);
    // }
}
