package com.example.flattie.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String joinCode; // Randomly generated code for joining the flat
    private String flatName; // Name of the flat

    @Column(unique = true) // Ensure the address is unique in the database
    private String address; // Address of the flat

    private String city; // City where the flat is located
    private String postcode; // Postcode of the flat
    private String flatDescription; // Description of the flat
    private double weeklyRent; // Weekly rent for the flat
    private int rooms; // Number of rooms in the flat

    @OneToOne(mappedBy = "flat", cascade = CascadeType.ALL)
    private ChoreList choreList; // Chore list associated with the flat

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rent_expense_id")
    private FlatExpense rentExpense; // Rent expense associated with the flat

    // Default constructor required by JPA
    protected Flat() {
    }

    // Constructor with all fields
    public Flat(String joinCode, String flatName, String address, String city, String postcode, String flatDescription,
            double weeklyRent, int rooms) {
        this.joinCode = joinCode;
        this.flatName = flatName;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.flatDescription = flatDescription;
        this.weeklyRent = weeklyRent;
        this.rooms = rooms;
        this.choreList = new ChoreList(this, new ArrayList<>()); // Initialize the chore list with an empty list
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public String getFlatName() {
        return flatName;
    }

    public void setFlatName(String flatName) {
        this.flatName = flatName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getFlatDescription() {
        return flatDescription;
    }

    public void setFlatDescription(String flatDescription) {
        this.flatDescription = flatDescription;
    }

    public double getWeeklyRent() {
        // Get the weekly rent from the rent expense if it exists
        return (rentExpense != null) ? rentExpense.getTotalAmount().doubleValue() : 0.0;
    }

    public void setWeeklyRent(double weeklyRent) {
        this.rentExpense.setTotalAmount(BigDecimal.valueOf(weeklyRent)); // Update the rent expense
        this.weeklyRent = weeklyRent; // Update the flat's weekly rent
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public ChoreList getChoreList() {
        return choreList;
    }

    public void setChoreList(ChoreList choreList) {
        this.choreList = choreList;
    }

    public FlatExpense getRentExpense() {
        return rentExpense;
    }

    public void setRentExpense(FlatExpense rentExpense) {
        this.rentExpense = rentExpense;
    }
}