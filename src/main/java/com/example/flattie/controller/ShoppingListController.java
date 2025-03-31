package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.repository.ShoppingListItemRepository;
// import com.example.flattie.config.SecurityConfig;
import com.example.flattie.model.ShoppingListItem;
import com.example.flattie.service.ShoppingListItemService;
// import com.example.flattie.service.userRepository;
// import org.springframework.web.bind.annotation.RequestBody;

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
}




	// @PostMapping("path")
	// public String postMethodName(@RequestBody String entity) {
	// //TODO: process POST request

	// return entity;
	// }

	// public String editQuantity(String itemName, int Quantity){

	// }

	// @PostMapping("path")
	// public String deleteItem(@RequestBody String entity) {
	// //TODO: process POST request

	// return entity;
	// }


