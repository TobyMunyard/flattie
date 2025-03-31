package com.example.flattie.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class ChoreList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "choreListID") // foreign key in ChoreListItem table
    private List<ChoreListItem> choreListItems;

    protected ChoreList() {
        // For JPA
        this.choreListItems = new ArrayList<>();
    }

    public ChoreList(Flat flat, ArrayList<ChoreListItem> choreListItems) {
        this.flat = flat;
        this.choreListItems = choreListItems;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public List<ChoreListItem> getChoreListItems() {
        return choreListItems;
    }

    public void setChoreListItems(List<ChoreListItem> choreListItems) {
        this.choreListItems = choreListItems;
    }

    public void addChore(ChoreListItem chore) {
        if (this.choreListItems == null) {
            this.choreListItems = new ArrayList<>();
        }
        chore.setChoreList(this); // Set the back-reference
        // Add the chore to the list
        // This is needed for the bi-directional relationship to work.
        // The chore list item needs to know which chore list it belongs to.
        this.choreListItems.add(chore);
    }

    public void removeChore(ChoreListItem chore) {
        this.choreListItems.remove(chore);
    }

}