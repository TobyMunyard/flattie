package com.example.flattie.service;

import com.example.flattie.repository.ChoreListItemRepository;
import com.example.flattie.repository.ChoreListRepository;
import com.example.flattie.model.ChoreList;
import com.example.flattie.model.ChoreListItem;
import com.example.flattie.model.Flat;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for database interaction with ChoreList entities.
 * Performs actual calling of ChoreListRepository methods.
 */
@Service
public class ChoreListService {
    
    private final ChoreListRepository choreListRepository;

    @Autowired
    private ChoreListItemRepository choreListItemRepository;

    @Autowired
    public ChoreListService(ChoreListRepository choreListRepository) {
        this.choreListRepository = choreListRepository;
    }

    /**
     * Gets all chore list items assigned to the given chore list ID.
     * 
     * @return A list of all current ChoreListItem entities associated with this
     *         chore list.
     */
    public List<ChoreListItem> getChoreListItems(Long choreListId) {
        return choreListRepository.findById(choreListId)
            .map(ChoreList::getChoreListItems)
            .orElse(new ArrayList<>());
    }

    /**
     * Gets the ChoreList for a given flat ID. If no ChoreList exists, a new one is
     * created and saved.
     * 
     * @param flatId The ID of the flat for which to retrieve the ChoreList.
     * @return The ChoreList associated with the given flat ID.
     */
    public ChoreList getChoreListForFlat(Long flatId) {
        return choreListRepository.findByFlatId(flatId);
    }

    public void addChoreToFlat(Flat flat, ChoreListItem choreItem) {
        ChoreList choreList = getChoreListForFlat(flat.getId());
        // Add the chore item to the flat's chore list
        choreList.getChoreListItems().add(choreItem);
        choreItem.setChoreList(choreList); // Ensure bi-directional link
        saveChoreList(choreList);
    }

    /**
     * Deletes a ChoreListItem from the ChoreList database based on a provided id.
     * 
     * @param id The id of the ChoreListItem to delete from the database.
     */
    public void deleteChoreFromFlat(Long choreItemId) {
        choreListItemRepository.deleteById(choreItemId);
    }

    /**
     * Saves a new ChoreList to the database.
     * 
     * @param choreList The ChoreList to persist in the database.
     */
    public void saveChoreList(ChoreList choreList) {
        choreListRepository.save(choreList);

    }
}
