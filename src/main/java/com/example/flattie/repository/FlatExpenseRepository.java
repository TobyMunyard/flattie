package com.example.flattie.repository;

import com.example.flattie.model.FlatExpense;

import com.example.flattie.model.Flat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlatExpenseRepository extends JpaRepository<FlatExpense, Long> {

    List<FlatExpense> findByFlat(Flat flat);

    List<FlatExpense> findByFlatAndExpenseMonth(Flat flat, LocalDate expenseMonth);

    List<FlatExpense> findByFlat_Id(Long flatId);
}
