package com.example.flattie.service;

import com.example.flattie.repository.ChoreListRepository;
import com.example.flattie.model.ChoreList;
import com.example.flattie.model.ChoreListItem;
import com.example.flattie.model.Flat;

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
    public ChoreListService(ChoreListRepository choreListRepository) {
        this.choreListRepository = choreListRepository;
    }

    /**
     * Gets all chore list items currently in the database.
     * 
     * @return A list of all current ChoreListItem entities associated with this chore list.
     */
    public List<ChoreListItem> getChoreListItems() {
        //Placeholder empty list to be replaced with actual data from the database.
        List<ChoreListItem> emptyList = List.of();
        return emptyList;
    }

    public ChoreList getChoreListForFlat(Long flatId) {
        return choreListRepository.findByFlatId(flatId);
    }

    public void addChoreToFlat(Flat flat, ChoreListItem choreItem) {
        // Add the chore item to the flat's chore list
        ChoreList choreList = getChoreListForFlat(flat.getId());
        choreList.addChore(choreItem);
        saveChoreList(choreList);;
    }

    /**
     * Deletes a ChoreListItem from the ChoreList database based on a provided id.
     * 
     * @param id The id of the ChoreListItem to delete from the database.
     */
    public void deleteChoreFromFlat(Long id) {
        choreListRepository.deleteById(id);
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
