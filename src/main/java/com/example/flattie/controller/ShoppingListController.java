package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.repository.ShoppingListItemRepository;
// import com.example.flattie.config.SecurityConfig;
import com.example.flattie.model.ShoppingListItem;
import com.example.flattie.service.ShoppingListItemService;
// import com.example.flattie.service.userRepository;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controller class for handling all actions related to users logging into their
 * accounts.
 */
@Controller
public class ShoppingListController {

	@Autowired
	private ShoppingListItemService shoppingListService;
	// private final UserRepository userRepository;

	/**
	 * Handles a request from a user to alter the current shopping list
	 */

	@PostMapping("/shopping-list/add")
	public ResponseEntity<ShoppingListItem> addItem(@RequestBody ShoppingListItem item) {
		System.out.println(item.toString());
		ShoppingListItem savedItem = shoppingListService.saveItem(item);
		return ResponseEntity.ok(savedItem);
	}

	@GetMapping("/shopping-list")
	public ResponseEntity<List<ShoppingListItem>> getAllItems() {
		List<ShoppingListItem> items = shoppingListService.getAllItems();
		return ResponseEntity.ok(items);
	}

	@DeleteMapping("/shopping-list/delete/{itemId}")
	public ResponseEntity<String> deleteItem(@PathVariable String itemId) {
		List<ShoppingListItem> items = shoppingListService.getAllItems();

		for (ShoppingListItem listItem : items) {
			if (listItem.getId() == Integer.parseInt(itemId)) {
				shoppingListService.deleteItem(listItem.getId());
				return ResponseEntity.ok("Item deleted successfully");
			}
		}
		return ResponseEntity.status(404).body("Item not found");
	}

	@PutMapping("/shopping-list/update/{id}")
	public ResponseEntity<String> updateItem(@PathVariable Long id, @RequestBody ShoppingListItem updatedItem) {
		ShoppingListItem existingItem = shoppingListService.getItembyId(id);
	
		if (existingItem != null) {
			if (updatedItem.getItemName() != null) {
				existingItem.setItemName(updatedItem.getItemName());
			}
			if (updatedItem.getQuantity() != 0) {
				existingItem.setQuantity(updatedItem.getQuantity());
			}
			shoppingListService.updateItem(existingItem);
			return ResponseEntity.ok("Item updated successfully");
		}
		return ResponseEntity.status(404).body("Item not found");
	}
}
	



