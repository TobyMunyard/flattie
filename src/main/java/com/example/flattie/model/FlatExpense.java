package com.example.flattie.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FlatExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flat_id")
    private Flat flat;

    @Column(nullable = false)
    private String name; // e.g., Rent, Power, Food

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private LocalDate month; // Optional - use for monthly tracking

    @OneToMany(mappedBy = "flatExpense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlatExpenseDelegation> delegations = new ArrayList<>();

    // Constructors
    public FlatExpense() {}

    public FlatExpense(Flat flat, String name, BigDecimal totalAmount, LocalDate month) {
        this.flat = flat;
        this.name = name;
        this.totalAmount = totalAmount;
        this.month = month;
    }

    // Getters and Setters...

    public void addDelegation(FlatExpenseDelegation delegation) {
        delegation.setFlatExpense(this);
        delegations.add(delegation);
    }

    public void removeDelegation(FlatExpenseDelegation delegation) {
        delegations.remove(delegation);
        delegation.setFlatExpense(null);
    }
}
