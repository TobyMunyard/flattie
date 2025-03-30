package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.flattie.repository.ShoppingListItemRepository;
// import com.example.flattie.config.SecurityConfig;
// import com.example.flattie.model.ShoppingListItem;
// import com.example.flattie.service.ShoppingListItemService;
// import com.example.flattie.service.userRepository;
// import org.springframework.web.bind.annotation.RequestBody;





/**
 * Controller class for handling all actions related to users logging into their accounts.
 */
@Controller
public class ShoppingListController {


	
	@Autowired
    private ShoppingListItemRepository shoppingListItemRepository;
	// private final UserRepository userRepository;


	/**
	 * Handles a request from a user to alter the current shopping list
	 */

	@PostMapping("/add-item")
	public String addItem(String itemName, int quantity){

	// User loggedInUser = userService.getLoggedInUser();  // Get the current user
	// 	ShoppingListItem newItem = new ShoppingListItem(itemName, quantity);

	// 	shoppingListItemService.saveItem(newItem);
	// 	return "redirect:/shopping-list";

	//
	return "ShoppingList"; 
	}


	// @PostMapping("path")
	// public String postMethodName(@RequestBody String entity) {
	// 	//TODO: process POST request
		
	// 	return entity;
	// }

	
	// public String editQuantity(String itemName, int Quantity){

	// }
	
	// @PostMapping("path")
	// public String deleteItem(@RequestBody String entity) {
	// 	//TODO: process POST request
		
	// 	return entity;
	// }
	
}
