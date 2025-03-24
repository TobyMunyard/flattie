package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;

/**
 * Controller class for handling all requests based on user account creation
 * within the system.
 */
@Controller
public class CreateAccountController {

    @Autowired
    AppUserService appUserService;

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
            @RequestParam("confirmPassword") String confirmPassword) {

        if (password.equals(confirmPassword)) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            AppUser newUser = new AppUser(firstName, lastName, username, passwordEncoder.encode(password));
            System.out.println("Working: " + newUser);
            appUserService.saveAppUser(newUser);
        }

        return "login";
    }
}
