package com.example.flattie.repository;

import com.example.flattie.model.FlatExpenseDelegation;
import com.example.flattie.model.AppUser;
import com.example.flattie.model.FlatExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlatExpenseDelegationRepository extends JpaRepository<FlatExpenseDelegation, Long> {

    List<FlatExpenseDelegation> findByFlatmate(AppUser flatmate);

    List<FlatExpenseDelegation> findByFlatExpense(FlatExpense flatExpense);

    List<FlatExpenseDelegation> findByFlatmate_Id(Long flatmateId);
}