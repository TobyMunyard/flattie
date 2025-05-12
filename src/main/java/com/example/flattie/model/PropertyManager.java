package com.example.flattie.model;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class PropertyManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "propertyManager", cascade = CascadeType.PERSIST)
    private List<Flat> flats;

    // Default constructor required by JPA
    public PropertyManager() {
        flats = new ArrayList<>();
    }

    // Constructor with all fields
    public PropertyManager(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        flats = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addFlat(Flat flat) {
        if (flats == null) {
            flats = new ArrayList<>();
        } else if (flats.contains(flat)) {
            return; // Flat already exists in the list, no need to add it again
        } else {
            this.flats.add(flat);
            flat.setPropertyManager(this); // Set the property manager for the flat as well
        }
    }

    public void removeFlat(Flat flat) {
        this.flats.remove(flat);
        flat.setPropertyManager(null); // Remove the property manager from the flat
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public void setFlatList(List<Flat> flats) {
        this.flats = flats;
    }
}