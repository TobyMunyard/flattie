package com.example.flattie.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class FlatExpenseDelegation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flat_expense_id")
    private FlatExpense flatExpense;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flatmate_id")
    private Flatmate flatmate;

    @Column(nullable = false)
    private BigDecimal amount;

    // Constructors
    public FlatExpenseDelegation() {}

    public FlatExpenseDelegation(Flatmate flatmate, BigDecimal amount) {
        this.flatmate = flatmate;
        this.amount = amount;
    }

    // Getters and Setters...
}
