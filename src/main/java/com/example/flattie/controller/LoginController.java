package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import com.example.flattie.config.SecurityConfig;
import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;

/**
 * Controller class for handling all actions related to users logging into their
 * accounts.
 */
@Controller
public class LoginController {

    @Autowired
    AppUserService appUserService;

    /**
     * Handles a request from a user to log into an account. Redirects differently
     * based on whether inputs are valid.
     * 
     * @param username           The username of the account a user is trying to log
     *                           into.
     * @param password           The password for the account a user is trying to
     *                           log into.
     * @param model              The model of the front-end of the application,
     *                           anything added to this can be accessed on other
     *                           pages.
     * @param redirectAttributes Essentially a one time version of the model,
     *                           anything added to this will only exist for one
     *                           redirect.
     * @return If username and password are correct, the joinFlat page. Otherwise,
     *         redirect to the login page with a error message displayed.
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
            Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        boolean usernameExists = true;
        boolean passwordsMatch = true;

        AppUser existingUser = appUserService.getAppUserByUsername(username).orElse(null);
        if (existingUser == null) {
            // Username does not exist, return login page again
            usernameExists = false;
            redirectAttributes.addFlashAttribute("usernameExists", usernameExists);
            return "redirect:/login";
        }

        if (!SecurityConfig.passwordEncoder().matches(password, existingUser.getPassword())) {
            // Password does not match saved one
            passwordsMatch = false;
            redirectAttributes.addFlashAttribute("passwordsMatch", passwordsMatch);
            return "redirect:/login";
        }

        // User exists and login correct
        session.setAttribute("user", existingUser);
        return "redirect:/joinFlat";
    }
}
