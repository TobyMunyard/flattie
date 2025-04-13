package com.example.flattie.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.FlatExpense;
import com.example.flattie.model.FlatExpenseDelegation;
import com.example.flattie.repository.AppUserRepository;
import com.example.flattie.repository.FlatExpenseDelegationRepository;
import com.example.flattie.repository.FlatExpenseRepository;

/**
 * Service class for managing flat expenses and their delegations. This class is
 * responsible for handling the business logic related to flat expenses, such as
 * saving delegations and validating expense data.
 */
@Service
public class FlatExpenseService {

    @Autowired
    private FlatExpenseRepository flatExpenseRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private FlatExpenseDelegationRepository flatExpenseDelegationRepository;

    /**
     * Saves the delegations for a given expense. This method validates the
     * delegations
     * and ensures that they are correctly associated with the expense and the
     * user's
     * flat. @Transactional ensures that the operation is atomic, meaning that if
     * any part
     * of the operation fails, the entire transaction will be rolled back.
     *
     * @param user        The current authenticated user.
     * @param expenseId   The ID of the expense to which the delegations belong.
     * @param delegations The list of delegations to be saved.
     * @return The updated FlatExpense object with the new delegations.
     */
    @Transactional
    public FlatExpense saveDelegations(AppUser user, Long expenseId, List<FlatExpenseDelegation> delegations) {
        /**
         * Validations needed:
         * - ensure the sum of all delegation amounts equate to the total amount of the
         * expense
         * - ensure the delegations are not null, empty, or have negative amounts
         * - ensure the flatmates in delegations are part of User's flat
         */
        // Fetch the expense by ID and check if it belongs to the user's flat
        FlatExpense expense = flatExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        // Check if delegations are null or empty
        if (delegations == null || delegations.isEmpty()) {
            throw new IllegalArgumentException("Delegations cannot be null or empty.");
        }

        // Check for negative amounts in delegations
        if (delegations.stream().anyMatch(d -> d.getAmount().compareTo(BigDecimal.ZERO) < 0)) {
            throw new IllegalArgumentException("Delegation amounts cannot be negative.");
        }

        // Calculate the total of all delegation amounts and ensure total matches
        // expense total
        BigDecimal total = delegations.stream()
                .map(FlatExpenseDelegation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(expense.getTotalAmount()) != 0) {
            throw new IllegalArgumentException("Delegation amounts must sum to the total expense amount.");
        }

        // Check if the delegations are associated with the user's flat and validate
        // each
        // delegation before saving
        for (FlatExpenseDelegation delegation : delegations) {
            Long flatmateId = delegation.getFlatmate() != null ? delegation.getFlatmate().getId() : null;
            if (flatmateId == null) {
                throw new IllegalArgumentException("Delegation must include a flatmate ID.");
            }

            AppUser flatmate = appUserRepository.findById(flatmateId)
                    .orElseThrow(() -> new IllegalArgumentException("Flatmate not found."));

            if (!Objects.equals(flatmate.getFlat().getId(), user.getFlat().getId())) {
                throw new IllegalArgumentException("Flatmate is not part of the current user's flat.");
            }

            delegation.setFlatmate(flatmate);
            delegation.setFlatExpense(expense);

        }

        // Save the delegations and update the expense with the new delegations
        expense.getDelegations().clear(); // remove old ones
        for (FlatExpenseDelegation delegation : delegations) {
            delegation.setFlatExpense(expense); // set the owning side
        }
        expense.getDelegations().addAll(delegations); // re-attach new ones
        flatExpenseDelegationRepository.saveAll(delegations);
        flatExpenseRepository.save(expense);

        // Return the updated expense with delegations
        return expense;
    }
}
