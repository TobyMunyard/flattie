// package com.example.flattie.controller;

// import com.example.flattie.model.AppUser;
// import com.example.flattie.model.Flat;
// import com.example.flattie.model.ShoppingListItem;
// import com.example.flattie.service.ShoppingListItemService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.HttpHeaders;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

// import java.util.List;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyInt;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// @WebMvcTest(ShoppingListController.class)
// class ShoppingListControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private ShoppingListItemService shoppingListService;

//     @Autowired
//     private ObjectMapper objectMapper;  // Provided by Spring Boot Test starter

//     private Flat testFlat;
//     private AppUser validUser;
//     private AppUser noFlatUser;
//     private ShoppingListItem testItem;

//     @BeforeEach
//     void setup() {
//         // Create a test flat with an ID (needed for redirection and association)
//         testFlat = new Flat("JOIN123", "Test Flat", "123 Main St", "Dunedin", "9016", "Sunny flat", 250.00, 4);
//         testFlat.setId(100L);

//         // validUser has a flat associated
//         validUser = new AppUser("user", "email", "User Name", "password");
//         validUser.setId(1L);
//         validUser.setFlat(testFlat);

//         // noFlatUser does NOT belong to any flat
//         noFlatUser = new AppUser("nouser", "email", "No Flat User", "password");
//         noFlatUser.setId(2L);
//         noFlatUser.setFlat(null);

//         // Create a test shopping list item
//         testItem = new ShoppingListItem("Milk", 2);
        
//     }

//     // Test for adding an item when the user is in a flat
//     @Test
//     void testAddItemWithValidUser() throws Exception {
//         // Stub the saveItem method to return our test item.
//         when(shoppingListService.saveItem(any(ShoppingListItem.class))).thenReturn(testItem);

//         mockMvc.perform(post("/shopping-list/add")
//                 .with(SecurityMockMvcRequestPostProcessors.user(validUser))
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(testItem)))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(testItem.getId()))
//                 .andExpect(jsonPath("$.itemName").value(testItem.getItemName()))
//                 .andExpect(jsonPath("$.quantity").value(testItem.getQuantity()));

//         verify(shoppingListService).saveItem(any(ShoppingListItem.class));
//     }

//     // Test for add endpoint when user has not joined a flat
//     @Test
//     void testAddItemWithoutFlat() throws Exception {
//         mockMvc.perform(post("/shopping-list/add")
//                 .with(SecurityMockMvcRequestPostProcessors.user(noFlatUser))
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(testItem)))
//                 .andDo(print())
//                 .andExpect(status().isForbidden())
//                 .andExpect(content().string("Access denied: Please join a flat before adding to the shopping list."));

//         verify(shoppingListService, never()).saveItem(any());
//     }

//     // Test for getting the shopping list when the user is valid and has a flat
//     @Test
//     void testGetShoppingListWithValidUser() throws Exception {
//         List<ShoppingListItem> itemList = List.of(testItem);
//         when(shoppingListService.getAllItems()).thenReturn(itemList);

//         mockMvc.perform(get("/shopping-list")
//                 .with(SecurityMockMvcRequestPostProcessors.user(validUser)))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].id").value(testItem.getId()))
//                 .andExpect(jsonPath("$[0].itemName").value(testItem.getItemName()))
//                 .andExpect(jsonPath("$[0].quantity").value(testItem.getQuantity()));

//         verify(shoppingListService).getAllItems();
//     }

//     // Test for getting the shopping list when the user is not authenticated
//     @Test
//     void testGetShoppingListWithoutAuthentication() throws Exception {
//         mockMvc.perform(get("/shopping-list"))
//                 .andDo(print())
//                 .andExpect(status().isFound())
//                 .andExpect(header().string(HttpHeaders.LOCATION, "/login"));
//     }

//     // Test for getting the shopping list when the user has not joined a flat
//     @Test
//     void testGetShoppingListWithoutFlat() throws Exception {
//         mockMvc.perform(get("/shopping-list")
//                 .with(SecurityMockMvcRequestPostProcessors.user(noFlatUser)))
//                 .andDo(print())
//                 .andExpect(status().isFound())
//                 .andExpect(header().string(HttpHeaders.LOCATION, "/joinFlat"));
//     }

//     // Test for delete endpoint when the item exists.
//     @Test
//     void testDeleteItem_ItemExists() throws Exception {
//         // The controller iterates over all items to find the matching item.
//         List<ShoppingListItem> itemList = List.of(testItem);
//         when(shoppingListService.getAllItems()).thenReturn(itemList);

//         mockMvc.perform(delete("/shopping-list/delete/" + testItem.getId()))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Item deleted successfully"));

//         verify(shoppingListService).deleteItem(testItem.getId());
//     }

//     // Test for delete endpoint when the item is not found.
//     @Test
//     void testDeleteItem_ItemNotFound() throws Exception {
//         when(shoppingListService.getAllItems()).thenReturn(List.of());

//         mockMvc.perform(delete("/shopping-list/delete/999"))
//                 .andDo(print())
//                 .andExpect(status().isNotFound())
//                 .andExpect(content().string("Item not found"));

//         verify(shoppingListService, never()).deleteItem((long)anyInt());
//     }

//     // Test for update endpoint when the item exists.
//     @Test
//     void testUpdateItem_ItemExists() throws Exception {
//         // Simulate an existing item
//         when(shoppingListService.getItembyId(testItem.getId())).thenReturn(testItem);
//         doNothing().when(shoppingListService).updateItem(any(ShoppingListItem.class));

//         ShoppingListItem updatedItem = new ShoppingListItem("bread", 5);

//         mockMvc.perform(put("/shopping-list/update/" + testItem.getId())
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(updatedItem)))
//                 .andDo(print())
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Item updated successfully"));

//         verify(shoppingListService).updateItem(any(ShoppingListItem.class));
//     }

//     // Test for update endpoint when the item does not exist.
//     @Test
//     void testUpdateItem_ItemNotFound() throws Exception {
//         when(shoppingListService.getItembyId(999L)).thenReturn(null);

//         ShoppingListItem updatedItem = new ShoppingListItem("bread", 5);
        

//         mockMvc.perform(put("/shopping-list/update/999")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(updatedItem)))
//                 .andDo(print())
//                 .andExpect(status().isNotFound())
//                 .andExpect(content().string("Item not found"));

//         verify(shoppingListService, never()).updateItem(any(ShoppingListItem.class));
//     }
// }
