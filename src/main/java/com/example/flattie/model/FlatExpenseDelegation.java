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
    @JoinColumn(name = "user_id")
    private AppUser flatmate;

    @Column(nullable = false)
    private BigDecimal amount;

    // Constructors
    public FlatExpenseDelegation() {
    }

    public FlatExpenseDelegation(AppUser flatmate, BigDecimal amount) {
        this.flatmate = flatmate;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlatExpense getFlatExpense() {
        return flatExpense;
    }

    public void setFlatExpense(FlatExpense flatExpense) {
        this.flatExpense = flatExpense;
    }

    public AppUser getFlatmate() {
        return flatmate;
    }

    public void setFlatmate(AppUser flatmate) {
        this.flatmate = flatmate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
