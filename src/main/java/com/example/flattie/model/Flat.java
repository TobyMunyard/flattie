package com.example.flattie.model;

import jakarta.persistence.*;

@Entity
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String joinCode;
    private double weeklyRent;
    private int rooms;

    protected Flat() {
    }

    public Flat(String joinCode, double weeklyRent, int rooms) {
        this.joinCode = joinCode;
        this.weeklyRent = weeklyRent;
        this.rooms = rooms;
    }

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

}
