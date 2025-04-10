package com.example.flattie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatExpense;
import com.example.flattie.service.AppUserService;

@Controller
/**
 * Controller class for Flat Expenses. Maps api calls to methods that handle
 * the requests and return the appropriate response.
 */
public class FlatExpenseController {

    @GetMapping("/api/flat/rent")
    @ResponseBody
    public BigDecimal getCurrentUserFlatRent(@AuthenticationPrincipal AppUser user, Model model) {
        // Might not need the model, but it's here for future use?
        Flat flat = user.getFlat(); // adjust to your method
        FlatExpense rentExpense = flat.getRentExpense();

        if (rentExpense != null && rentExpense.getTotalAmount() != null) {
            return rentExpense.getTotalAmount();
        } else {
            return BigDecimal.ZERO;
        }
    }
}
