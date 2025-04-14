package com.example.flattie.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

        // In case no errors occur this will satisfy thymeleaf
        model.addAttribute("error", null);

        // Check username is unique
        Optional<AppUser> optionalUser = appUserService.getAppUserByUsername(username);

        if (optionalUser.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username in use.");
            return "redirect:/createAccount";
        }

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
     * Creates two default user accounts for testing purposes. This method is called
     * when the application starts up.
     */
    @PostConstruct
    public void createDefaultUsers() {
        // Check if the test accounts already exist
        Optional<AppUser> existingUser1 = appUserService.getAppUserByUsername("Tester");
        Optional<AppUser> existingUser2 = appUserService.getAppUserByUsername("JaneDoe");

        /**
         * If they don't exist, create them
         * Note: Passwords are hardcoded for testing purposes. In a real application,
         * these are encrypted, obviously.
         */
        if (existingUser1.isEmpty()) {
            AppUser user1 = new AppUser("Test", "User", "Tester", passwordEncoder.encode("test1234"));
            appUserService.saveAppUser(user1);
            System.out.println("Test account created: Tester / test1234");
        }

        if (existingUser2.isEmpty()) {
            AppUser user2 = new AppUser("Jane", "Doe", "JaneDoe", passwordEncoder.encode("pass123"));
            appUserService.saveAppUser(user2);
            System.out.println("Test account created: JaneDoe / pass123");
        }
    }

    /**
     * Called when a user submits the edit account form. Can update first name, last
     * name and username. Password is updated seperately due to security reasons, as
     * displaying it is not possible and a huge security flaw.
     * 
     * @param authenticatedUser The user logged in currently.
     * @param firstName         The first name of the user.
     * @param lastName          The last name of the user.
     * @param username          The username for the user's account.
     * @return A redirect back to the profile page.
     */
    @PostMapping("/updateUser")
    public String updateUserInfo(@AuthenticationPrincipal AppUser authenticatedUser,
            @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
            @RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        // Get user by AuthenticationPrincipal in case username is being changed
        AppUser existingUser = appUserService.getAppUserById(authenticatedUser.getId())
                .orElse(null);
        if (existingUser == null) {
            // Should not be possible, display error page.
            return "error";
        }

        // Check username is unique
        Optional<AppUser> optionalUser = appUserService.getAppUserByUsername(username);

        if (optionalUser.isPresent() && !(authenticatedUser.getUsername().equals(username))) {
            redirectAttributes.addFlashAttribute("error", "Username in use.");
            return "redirect:/profilePage";
        }

        // Other validation

        if (firstName == null || firstName.isBlank() || firstName.length() > 20) {
            redirectAttributes.addFlashAttribute("error", "First name must be under 20 characters.");
            return "redirect:/profilePage";
        }

        if (lastName == null || lastName.isBlank() || lastName.length() > 40) {
            redirectAttributes.addFlashAttribute("error", "Last name must be under 40 characters.");
            return "redirect:/profilePage";
        }

        if (username == null || username.isBlank() || username.length() < 5 || username.length() > 20) {
            redirectAttributes.addFlashAttribute("error", "Username must be between 5 and 20 characters.");
            return "redirect:/profilePage";
        }

        // Update user information and redirect to the same page so they can see results
        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setUsername(username);
        appUserService.saveAppUser(existingUser);

        // Refreshes the whole @AuthenticationPrincipal, refreshing user information for
        // display instead of requiring logout to see changes.
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                existingUser, null, existingUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return "redirect:/profilePage";
    }

    /**
     * Called when a user submits the change password form. Performs same checks as
     * account creation to ensure values are valid and then makes the changes to the
     * users password.
     * 
     * @param authenticatedUser  The user logged in currently.
     * @param password           The new password entered by the user.
     * @param repeatedPassword   The repeat password entry to ensuring correct value
     *                           is saved.
     * @param redirectAttributes Attirbutes of the page that will disappear after a
     *                           redirect, used to display an error (if there is
     *                           one).
     * @return A redirect to the user profile page.
     */
    @PostMapping("/changePassword")
    public String changePassword(@AuthenticationPrincipal AppUser authenticatedUser,
            @RequestParam("password") String password, @RequestParam("repeatedPassword") String repeatedPassword,
            RedirectAttributes redirectAttributes) {
        // Get user by AuthenticationPrincipal in case username is being changed
        AppUser existingUser = appUserService.getAppUserById(authenticatedUser.getId())
                .orElse(null);
        if (existingUser == null) {
            // Should not be possible, display error page.
            return "error";
        }

        if (password == null || password.isBlank() || password.length() < 7 || password.length() > 50) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 7 characters long.");
            return "redirect:/profilePage";
        }

        if (!password.equals(repeatedPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/profilePage";
        }

        // Inputs are valid, change the password and update the user
        existingUser.setPassword(passwordEncoder.encode(password));
        appUserService.saveAppUser(existingUser);

        // Refreshes the whole @AuthenticationPrincipal, refreshing user information for
        // display instead of requiring logout to see changes.
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                existingUser, null, existingUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return "redirect:/profilePage";
    }
}
