package com.example.flattie.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

/**
 * Database entity representing a shopping list item. Mapped automatically to
 * database schema using ShoppingListItemRepository and ShoppingListItemService.
 * 
 */
@Entity
public class ShoppingListItem {

    // Automatically generated by the database so is not needed in constructor.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int quantity;

    /**
     * Constructs a new shopping list item. Needed for spring to function.
     */
    protected ShoppingListItem() {
    }

    /**
     * Constructs a new shopping list item.
     */
    public ShoppingListItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString(){
       return "Name is: " + getItemName() + "Quantity is: " + getQuantity();
    }
}
