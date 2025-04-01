package com.example.flattie.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.flattie.model.ShoppingListItem;
import com.example.flattie.repository.ShoppingListItemRepository;

/**
 * Service class for database interaction with ShoppingListItem entities.
 * Performs actual calling of ShoppingListItemRepository methods.
 */
@Service
public class ShoppingListItemService {
    private final ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    public ShoppingListItemService(ShoppingListItemRepository shoppingListItemRepository) {
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    /**
     * Gets all shopping list items currently in the database.
     * 
     * @return A list of all current ShoppingListItem entities in the database.
     */
    public List<ShoppingListItem> getAllItems() {
        return shoppingListItemRepository.findAll();
    }

    /**
     * Saves a new ShoppingListItem to the database.
     * 
     * @param shoppingListItem The ShoppingListItem to persist in the database.
     * @return The saved entity.
     */
    public ShoppingListItem saveItem(ShoppingListItem shoppingListItem) {
        return shoppingListItemRepository.save(shoppingListItem);
    }

    /**
     * Deletes a ShoppingListItem from the databse based on a provided id.
     * 
     * @param id The id of the ShoppingListItem to delete from the database.
     */
    public void deleteItem(Long id) {
        shoppingListItemRepository.deleteById(id);
    }

    @SuppressWarnings("deprecation")
    public ShoppingListItem getItembyId(Long id){
        return shoppingListItemRepository.getById(id);
    }

    public ShoppingListItem updateItem(ShoppingListItem shoppingListItem) {
        return shoppingListItemRepository.save(shoppingListItem);
            }

}
