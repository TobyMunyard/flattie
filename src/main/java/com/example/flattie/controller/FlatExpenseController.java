package com.example.flattie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatExpense;
import com.example.flattie.model.FlatExpenseDelegation;
import com.example.flattie.repository.FlatExpenseRepository;
import com.example.flattie.service.FlatExpenseService;

@Controller
/**
 * Controller class for Flat Expenses. Maps api calls to methods that handle
 * the requests and return the appropriate response. Also handles the expense
 * delegation to flatmates.
 */
public class FlatExpenseController {

    @Autowired
    private FlatExpenseRepository flatExpenseRepository;

    @Autowired
    private FlatExpenseService flatExpenseService;

    @GetMapping("/api/flat/rent")
    @ResponseBody
    public BigDecimal getCurrentUserFlatRent(@AuthenticationPrincipal AppUser user, Model model) {
        // Might not need the model, but it's here for future use?
        Flat flat = user.getFlat();
        FlatExpense rentExpense = flat.getRentExpense();

        if (rentExpense != null && rentExpense.getTotalAmount() != null) {
            return rentExpense.getTotalAmount();
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Returns the current user's flat expenses. This method is called when the
     * user accesses the /api/flat/expenses endpoint.
     *
     * @param user  The current authenticated user.
     * @param model The model to be used in the view.
     * @return A list of FlatExpense objects representing the user's flat expenses
     *         minus the rent expense.
     */
    @GetMapping("/api/flat/expenses")
    @ResponseBody
    public List<FlatExpense> getCurrentUserFlatExpenses(@AuthenticationPrincipal AppUser user, Model model) {
        // Might not need the model, but it's here for future use?
        Flat flat = user.getFlat();
        List<FlatExpense> expenses = flatExpenseRepository.findByFlat(flat);

        // Filter out the rent expense from the list of expenses
        expenses.removeIf(expense -> expense.getName().equals("Rent"));

        return expenses;
    }

    /**
     * Sets the flat expense delegations, this is a method that interacts with the
     * FlatExpenseService to save the delegations for a specific expense. It takes
     * the expense ID and a list of delegations as input, and returns the updated
     * FlatExpense object.
     */
    @PostMapping("/api/flat/expense/delegations")
    @ResponseBody
    public ResponseEntity<?> setFlatExpenseDelegations(
            @AuthenticationPrincipal AppUser user,
            @RequestParam("expenseId") Long expenseId,
            @RequestBody List<FlatExpenseDelegation> delegations) {
        try {
            FlatExpense updated = flatExpenseService.saveDelegations(user, expenseId, delegations);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Returns the rent expense ID for the current user's flat. This method is
     * called when the user accesses the /api/flat/rent-expense-id endpoint.
     *
     * @param user The current authenticated user.
     * @return The ID of the rent expense associated with the user's flat.
     */
    @GetMapping("/api/flat/rent-expense-id")
    @ResponseBody
    public Long getRentExpenseId(@AuthenticationPrincipal AppUser user) {
        return user.getFlat().getRentExpense().getId();
    }
}
