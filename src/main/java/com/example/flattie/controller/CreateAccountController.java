package com.example.flattie.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;

import jakarta.annotation.PostConstruct;

/**
 * Controller class for handling all requests based on user account creation
 * within the system.
 */
@Controller
public class CreateAccountController {

    @Autowired
    AppUserService appUserService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Called upon the submission of the account creation form. Checks that input
     * values are valid and passwords match.
     * 
     * @param firstName       The first name of the user.
     * @param lastName        The last name of the user.
     * @param username        The username for the user's account.
     * @param password        The password for the user's account.
     * @param confirmPassword A duplicate password input to ensure the value input
     *                        is correct.
     * @return If details are valid, redirect to the login page. Otherwise, redirect
     *         back to the account creation page with the error displayed.
     */
    @PostMapping("/createAccount")
    public String createAccount(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
            @RequestParam("username") String username, @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword, Model model,
            RedirectAttributes redirectAttributes) {

        // In case no errors occur this will satisy thymeleaf
        model.addAttribute("error", null);

        // Validation done manually because annotations only work for entire entity and
        // manual error messages are nicer for users

        if (firstName == null || firstName.isBlank() || firstName.length() > 20) {
            redirectAttributes.addFlashAttribute("error", "First name must be under 20 characters.");
            return "redirect:/createAccount";
        }

        if (lastName == null || lastName.isBlank() || lastName.length() > 40) {
            redirectAttributes.addFlashAttribute("error", "Last name must be under 40 characters.");
            return "redirect:/createAccount";
        }

        if (username == null || username.isBlank() || username.length() < 5 || username.length() > 20) {
            redirectAttributes.addFlashAttribute("error", "Username must be between 5 and 20 characters.");
            return "redirect:/createAccount";
        }

        if (password == null || password.isBlank() || password.length() < 7 || password.length() > 50) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 7 characters long.");
            return "redirect:/createAccount";
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/createAccount";
        }

        // User inputs are all acceptable, create account
        AppUser newUser = new AppUser(firstName, lastName, username, passwordEncoder.encode(password));
        appUserService.saveAppUser(newUser);

        return "redirect:/login";
    }


    /**
     * Creates a default user account for testing purposes. This method is called
     * when the application starts up.
     */
    @PostConstruct
    public void createDefaultUser() {
        Optional<AppUser> existingUser = appUserService.getAppUserByUsername("admin");

        if (existingUser.isEmpty()) {
            AppUser admin = new AppUser("Test", "User", "Tester", passwordEncoder.encode("test1234"));
            appUserService.saveAppUser(admin);
            System.out.println("Test account created: Tester / test1234");
        }
    }
}
