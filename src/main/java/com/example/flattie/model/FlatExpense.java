package com.example.flattie.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flat_expense", schema = "PUBLIC")
public class FlatExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flat_id")
    private Flat flat;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "expense_month")
    private LocalDate expenseMonth;

    @OneToMany(mappedBy = "flatExpense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlatExpenseDelegation> delegations = new ArrayList<>();

    public FlatExpense() {

    }

    public FlatExpense(Flat flat, String name, BigDecimal totalAmount, LocalDate expenseMonth) {
        this.flat = flat;
        this.name = name;
        this.totalAmount = totalAmount;
        this.expenseMonth = expenseMonth;
    }

    // Getters and setters...
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getExpenseMonth() {
        return expenseMonth;
    }

    public void setExpenseMonth(LocalDate expenseMonth) {
        this.expenseMonth = expenseMonth;
    }

    public List<FlatExpenseDelegation> getDelegations() {
        return delegations;
    }

    // public void setDelegations(List<FlatExpenseDelegation> delegations) {
    //     this.delegations = delegations;
    // }

    public void addDelegation(FlatExpenseDelegation delegation) {
        delegation.setFlatExpense(this);
        delegations.add(delegation);
    }

    public void removeDelegation(FlatExpenseDelegation delegation) {
        delegations.remove(delegation);
        delegation.setFlatExpense(null);
    }

}
