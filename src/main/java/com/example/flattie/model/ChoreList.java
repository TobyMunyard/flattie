package com.example.flattie.model;

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
    @JoinColumn(name = "choreListID")  // foreign key in ChoreListItem table
    private List<ChoreListItem> choreListItems;

    public ChoreList(Flat flat, List<ChoreListItem> choreListItems) {
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
        this.choreListItems.add(chore);
    }

    public void removeChore(ChoreListItem chore) {
        this.choreListItems.remove(chore);
    }

}