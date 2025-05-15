package com.example.flattie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.flattie.service.FlatService;
import com.example.flattie.util.ResponseApi;

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

    @Autowired
    private FlatService flatService;

    /**
     * Returns the current user's flat rent. This method is called when the user
     * accesses the /api/flat/rent endpoint.
     *
     * @param user The current authenticated user.
     * @return The total amount of the rent expense for the user's flat.
     */
    @GetMapping("/api/flat/rent")
    @ResponseBody
    public BigDecimal getCurrentUserFlatRent(@AuthenticationPrincipal AppUser user) {
        Flat flat = flatService.getFlatWithRentExpense(user.getFlat().getId());
        FlatExpense rentExpense = flat.getRentExpense();
        return rentExpense != null && rentExpense.getTotalAmount() != null
                ? rentExpense.getTotalAmount()
                : BigDecimal.ZERO;

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
    public List<Map<String, Object>> getCurrentUserFlatExpenses(@AuthenticationPrincipal AppUser user, Model model) {
        // Might not need the model, but it's here for future use?
        Flat flat = user.getFlat();
        List<FlatExpense> expenses = flatExpenseRepository.findByFlat(flat);

        // Filter out the rent expense from the list of expenses
        expenses.removeIf(expense -> expense.getName().equals("Rent"));

        return expenses.stream().map(expense -> {
            Map<String, Object> expenseMap = new HashMap<>();
            expenseMap.put("id", expense.getId());
            expenseMap.put("name", expense.getName());
            expenseMap.put("totalAmount", expense.getTotalAmount());
            expenseMap.put("expenseMonth", expense.getExpenseMonth());
            return expenseMap;
        }).toList();
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
            flatExpenseService.saveDelegations(user, expenseId, delegations);
            return ResponseEntity.ok(ResponseApi.success("Delegations saved successfully.")); // ✅
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ResponseApi.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseApi.error("Failed to save delegations: " + e.getMessage()));
        }
    }

    /**
     * Returns the delegations for a specific expense. This method is called when
     * the
     * user accesses the /api/flat/expense/delegations endpoint.
     *
     * @param expenseId The ID of the expense for which to retrieve delegations.
     * @return A list of delegations associated with the specified expense.
     */
    @GetMapping("/api/flat/expense/delegations")
    @ResponseBody
    public List<Map<String, Object>> getDelegations(@RequestParam("expenseId") Long expenseId) {
        FlatExpense expense = flatExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        // Only return the flatmate ID and amount (no circular reference risk)
        return expense.getDelegations().stream()
                .map(d -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("flatmate", Map.of("id", d.getFlatmate().getId()));
                    data.put("amount", d.getAmount());
                    return data;
                })
                .toList();
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
        Flat flat = flatService.getFlatWithRentExpense(user.getFlat().getId());
        return flat.getRentExpense().getId();
    }

    /**
     * Creates a new flat expense. This method is called when the user accesses the
     * /api/flat/expense/create endpoint.
     *
     * @param user The current authenticated user.
     * @param body The request body containing the expense details.
     * @return A response entity containing the ID of the created expense or an
     *         error message.
     */
    @PostMapping("/api/flat/expense/create")
    @ResponseBody
    public ResponseEntity<?> createExpense(
            @AuthenticationPrincipal AppUser user,
            @RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            String amountStr = body.get("totalAmount");
            String monthStr = body.get("expenseMonth");

            BigDecimal amount = new BigDecimal(amountStr);
            LocalDate expenseMonth = (monthStr != null && !monthStr.isBlank())
                    ? LocalDate.parse(monthStr)
                    : LocalDate.now();

            FlatExpense expense = flatExpenseService.createExpense(user, name, amount, expenseMonth);
            return ResponseEntity.ok(ResponseApi.success("Expense created successfully.",
                    Map.of("expenseId", expense.getId())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseApi.error("Failed to create expense: " + e.getMessage()));
        }
    }

    /**
     * Deletes a flat expense. This method is called when the user accesses the
     * /api/flat/expense/delete endpoint.
     *
     * @param user The current authenticated user.
     * @param id   The ID of the expense to delete.
     * @return A response entity indicating the success or failure of the deletion.
     */
    @PostMapping("/api/flat/expense/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteExpense(
            @AuthenticationPrincipal AppUser user,
            @PathVariable("id") Long id) {
        try {
            flatExpenseService.deleteExpense(user, id);
            return ResponseEntity.ok(ResponseApi.success("Expense deleted successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseApi.error("Failed to delete expense: " + e.getMessage()));
        }
    }
}
