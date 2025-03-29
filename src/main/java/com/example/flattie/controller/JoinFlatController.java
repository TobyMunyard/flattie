package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.Flat;
import com.example.flattie.service.FlatService;

@Controller
public class JoinFlatController {

    @Autowired
    private FlatService flatService;

    @PostMapping("/joinFlat")
    public String joinFlat(@RequestParam("flat_code") String flatCode, Model model) {
        // Retrieve the flat by its join code
        Flat flat = flatService.findByJoinCode(flatCode);
    
        if (flat == null) {
            // If no flat is found, add an error message
            model.addAttribute("error", "Invalid flat code. Please try again.");
            return "joinFlat";
        }
    
        // Pass the flat information to the view
        model.addAttribute("flat", flat);
        return "joinFlat"; // Render the same page with flat information
    }
}