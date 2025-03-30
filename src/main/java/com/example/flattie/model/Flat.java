package com.example.flattie.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
        return weeklyRent;
    }

    public void setWeeklyRent(double weeklyRent) {
        this.weeklyRent = weeklyRent;
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
}