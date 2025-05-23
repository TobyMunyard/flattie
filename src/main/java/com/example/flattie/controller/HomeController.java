package com.example.flattie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.repository.FlatRepository;

/**
 * Controller class for the main page of the application. Maps URLs to html
 * pages.
 */
@Controller
public class HomeController {

    @Autowired
    FlatRepository flatRepository;

    /**
     * Serves the home page of the application from the url "/". "index" string is
     * automatically mapped to file "index.html" in resources/templates folder.
     * 
     * @return The index page of the application.
     */
    @GetMapping("/")
    public String home(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);

        if (user == null || user.getFlat() == null) {
            model.addAttribute("flat", null);
            return "index";
        }
    
        Flat flat = flatRepository.findByIdWithNotices(user.getFlat().getId())
            .orElseThrow(() -> new RuntimeException("Flat not found"));
    
        model.addAttribute("flat", flat);
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
    public String joinFlat(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        return "joinFlat";
    }

    /**
     * Serves the profile page of the application from the url "/profilePage".
     * "profile" string is automatically mapped to file "profile.html" in
     * resources/templates folder.
     * 
     * @return The profile page for a user.
     */
    @GetMapping("/profilePage")
    public String profilePage(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Serves the flat info page of the application from the url "/flatInfo".
     * "flatInfo" string is automatically mapped to file "flatInfo.html" in
     * resources/templates folder.
     * 
     * @return The flat info page of the application.
     */
    // @GetMapping("/showFlatInfo")
    // public String flatInfo(@AuthenticationPrincipal AppUser user, Model model) {
    // model.addAttribute("user", user);
    // return "flatInfo";
    // }

    /**
     * Serves the shopping list page of the application from the url
     * "/shoppingList". "shoppingList" string is automatically mapped to file
     * "shoppingList.html" in resources/templates folder.
     * 
     * @return The index page of the application.
     */
    @GetMapping("/shoppingList")
    public String shoppingList(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        return "shoppingList";
    }

    /**
     * Serves the rent calculator page of the application from the url
     * "/rentCalculator".
     * "rentCalculator" string is automatically mapped to file "rentCalculator.html"
     * in
     * resources/templates folder.
     * 
     * @return The rent calculator page of the application.
     */
    @GetMapping("/rentCalculator")
    public String rentCalculator(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        return "rentCalculator";
    }

    /**
     * Serves the expenses page of the application from the url
     * "/expense". "expense" string is automatically mapped to file "expense.html"
     * in
     * resources/templates folder.
     * 
     * @return The expense page of the application.
     * @param user The current authenticated user.
     */
    @GetMapping("/expense")
    public String expense(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        return "expense";
    }

    /**
     * Serves the viewFlats page of the application from the url "/viewFlats".
     * "viewFlats" string is automatically mapped to file "viewFlats.html" in
     * resources/templates folder.
     * 
     * @return The viewFlats page of the application.
     */
    @GetMapping("/viewFlats")
    public String viewFlats(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        return "viewFlats";
    }

    /**
     * Serves the about page of the application from the url "/about". "about"
     * string is
     * automatically mapped to file "about.html" in resources/templates folder.
     * 
     * @return The about page of the application.
     */
    @GetMapping("/about")
    public String about(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        return "about";
    }

    /**
     * Serves the about page of the application from the url "/calendar". "calendar"
     * string is
     * automatically mapped to file "calendar.html" in resources/templates folder.
     * 
     * @return The calender page of the application.
     */
    @GetMapping("/calendar")
    public String calendar(@AuthenticationPrincipal AppUser user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("flatId", user.getFlat().getId());
        return "calendar";
    }
}
