package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.flattie.model.AppUser;
import com.example.flattie.model.ShoppingListItem;
import com.example.flattie.service.ShoppingListItemService;
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
	public ResponseEntity<?> addItem(@AuthenticationPrincipal AppUser user, @RequestBody ShoppingListItem item) {
		if (user == null || user.getFlat() == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Access denied: Please join a flat before adding to the shopping list.");
		}

		ShoppingListItem savedItem = shoppingListService.saveItem(item);
		return ResponseEntity.ok(savedItem);
	}

	@DeleteMapping("/shopping-list/delete/{itemId}")
	public ResponseEntity<String> deleteItem(@PathVariable("itemId") String itemId) {
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
	public ResponseEntity<String> updateItem(@PathVariable("id") Long id, @RequestBody ShoppingListItem updatedItem) {
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

	/**
	 * Handles a request from a user to view the flat shopping list
	 * 
	 * @param user The currently authenticated user
	 * @return A list of shopping list items for the user's flat, or a redirect
	 */
	@GetMapping("/shopping-list")
	public ResponseEntity<List<ShoppingListItem>> getShoppingList(@AuthenticationPrincipal AppUser user) {
		if (user == null) {
			// User is not signed in, redirect to login page
			return ResponseEntity.status(HttpStatus.FOUND) // 302 Found - redirect status
					.header(HttpHeaders.LOCATION, "/login")
					.build(); // Empty body since it's a redirect
		}

		if (user.getFlat() == null) {
			// User is not in a flat, redirect to joinFlat page
			return ResponseEntity.status(HttpStatus.FOUND) // 302 Found - redirect status
					.header(HttpHeaders.LOCATION, "/joinFlat")
					.build(); // Empty body since it's a redirect
		}

		// User is authenticated and in a flat, return shopping list items
		List<ShoppingListItem> shoppingList = shoppingListService.getAllItems();
		return ResponseEntity.ok(shoppingList); // Return the list as JSON
	}
}