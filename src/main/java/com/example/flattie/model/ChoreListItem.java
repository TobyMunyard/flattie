package com.example.flattie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Database entity representing a chore list item. Mapped automatically to
 * database schema using ChoreListItemRepository and ChoreListItemService.
 * 
 * @see com.example.flattie.repository.ChoreListItemRepository
 * @see com.example.flattie.service.ChoreListItemService
 */
@Entity
public class ChoreListItem {

    // Automatically generated by the database so is not needed in constructor.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Chore name cannot be empty")
    private String choreName;

    private String assignment = "Unassigned"; // Default value
    
    private int frequency = 0; // in days, default to 0 for no frequency

    private boolean isCompleted = false; // Default value, user wont add a completed chore.
    
    @NotNull
    @Min(value = 0, message = "Priority must be at least 0")
    @Max(value = 9, message = "Priority cannot be greater than 9")
    private int priority = 0; // Default value

    // The chore list this item belongs to.
    // This is a foreign key in the database.
    // CascadeType.MERGE is used to update the chore list when this item is updated.
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "choreListID", referencedColumnName = "id")
    private ChoreList choreList;

    /**
     * Constructs a new chore list item. Needed for spring to function.
     */
    protected ChoreListItem() {
    }

    /**
     * Constructs a new chore list item.
     */
    public ChoreListItem(String choreName, String assignment, int priority, int frequency) {
        this.choreName = choreName;
        this.assignment = assignment;
        this.priority = priority;
        this.frequency = frequency;
        this.isCompleted = false;  // Default to not completed, as a user wont add a completed chore.
    }

    /**
     * Returns the ID of the chore
     * 
     * @return the ID of the chore
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the chore
     * 
     * @param id the ID of the chore
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the chore.
     * 
     * 
     * @return the name of the chore
     */
    public String getChoreName() {
        return choreName;
    }

    /**
     * Sets the name of the chore.
     * 
     * @param choreName the name of the chore
     */
    public void setChoreName(String choreName) {
        this.choreName = choreName;
    }

    /**
     * Returns the flatmate assignment of the chore.
     * 
     * @return person assigned to the chore
     */
    public String getAssignment() {
        return assignment;
    }

    /**
     * Sets the assignment of the chore.
     * 
     * @param assignment the assignment of the chore
     */
    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    /**
     * Returns the priority of the chore.
     * 
     * @return the priority of the chore
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the chore.
     * 
     * @param priority the priority of the chore
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the frequency of the chore.
     * 
     * @return the frequency of the chore
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of the chore.
     * 
     * @param frequency the frequency of the chore
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Sets whether or not the chore is completed.
     * 
     * @param isCompleted whether or not the chore is completed
     */
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * Returns whether or not the chore is completed.
     * 
     * @return whether or not the chore is completed
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Returns the ID of the chore list this item belongs to.
     * 
     * @return the ID of the chore list this item belongs to
     */
    public ChoreList getChoreList() {
        return choreList;
    }

    /**
     * Sets the chore list this item belongs to.
     * 
     * @param choreList the chore list this item belongs to
     */
    public void setChoreList(ChoreList choreList) {
        this.choreList = choreList;
    }

    /**
     * Returns a string representation of the chore list item.
     * 
     * @return a string representation of the chore list item
     */
    @Override
    public String toString() {
        return String.format("ChoreListItem[id=%d, name='%s', assignedTo='%s']", id, choreName, assignment);
    }
}
