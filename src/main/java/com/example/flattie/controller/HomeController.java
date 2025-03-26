package com.example.flattie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import com.example.flattie.model.AppUser;

/**
 * Controller class for the main page of the application. Maps URLs to html
 * pages.
 */
@Controller
public class HomeController {

    /**
     * Serves the home page of the application from the url "/". "index" string is
     * automatically mapped to file "index.html" in resources/templates folder.
     * 
     * @return The index page of the application.
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Serves the login page of the application from the url "/login". "login"
     * string is automatically mapped to file "login.html" in resources/templates
     * folder.
     * 
     * @return The login page of the application.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Serves the create account page of the application from the url
     * "/createAccount". "createAccount" string is automatically mapped to file
     * "createAccount.html" in resources/templates
     * folder.
     * 
     * @return The createAccount page of the application.
     */
    @GetMapping("/createAccount")
    public String createAccount() {
        return "createAccount";
    }

    /**
     * Serves the join flat page of the application from the url "/joinFlat".
     * "joinFlat" string is automatically mapped to file "joinFlat.html" in
     * resources/templates folder.
     * 
     * @return The join flat page of the application.
     */
    @GetMapping("/joinFlat")
    public String joinFlat(HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        return "joinFlat";
    }

    /**
     * Serves the flat info page of the application from the url "/flatInfo".
     * "flatInfo" string is automatically mapped to file "flatInfo.html" in
     * resources/templates folder.
     * 
     * @return The flat info page of the application.
     */
    @GetMapping("/flatInfo")
    public String flatInfo(HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        return "flatInfo";
    }

    /**
     * Serves the shopping list page of the application from the url
     * "/shoppingList". "shoppingList" string is automatically mapped to file
     * "shoppingList.html" in resources/templates folder.
     * 
     * @return The index page of the application.
     */
    @GetMapping("/shoppingList")
    public String shoppingList(HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        return "shoppingList";
    }

    /**
     * Serves the chore list page of the application from the url "/choreList".
     * "choreList" string is automatically mapped to file "choreList.html" in
     * resources/templates folder.
     * 
     * @return The choreList page of the application.
     */
    @GetMapping("/choreList")
    public String choreList(HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        return "choreList";
    }

    /**
     * Serves the rent calculator page of the application from the url "/rentCalculator".
     * "rentCalculator" string is automatically mapped to file "rentCalculator.html" in
     * resources/templates folder.
     * 
     * @return The rent calculator page of the application.
     */
    @GetMapping("/rentCalculator")
    public String rentCalculator(HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        return "rentCalculator";
    }

    /**
     * Serves the viewFlats page of the application from the url "/viewFlats".
     * "viewFlats" string is automatically mapped to file "viewFlats.html" in
     * resources/templates folder.
     * 
     * @return The viewFlats page of the application.
     */
    @GetMapping("/viewFlats")
    public String viewFlats(HttpSession session) {
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        return "viewFlats";
    }
}
