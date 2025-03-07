package com.example.flattie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;;

/**
 * Controller class for the main page of the application. Maps URLs to html
 * pages.
 */
@Controller
public class HomeController {

    /**
     * Serves the home page of the application from the url "/". "Index" string is
     * automatically mapped to file "index.html" in resources/templates folder.
     * 
     * @return The index page of the application.
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
