package com.example.flattie.controller;

import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void testLoginWithValidCredentials() throws Exception {
        AppUser user = new AppUser("Cam", "Clark", "camerrs", "encodedPassword");

        Mockito.when(appUserService.getAppUserByUsername("camerrs"))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches("ThisIsASecurePassword", "encodedPassword"))
                .thenReturn(true);

        mockMvc.perform(post("/login")
                .param("username", "camerrs")
                .param("password", "ThisIsASecurePassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testLoginWithNonExistentUsername() throws Exception {
        Mockito.when(appUserService.getAppUserByUsername("unknownUser"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                .param("username", "unknownUser")
                .param("password", "anything"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("usernameExists", false));
    }

    @Test
    void testLoginWithIncorrectPassword() throws Exception {
        AppUser user = new AppUser("Cam", "Clark", "camerrs", "encodedPassword");

        Mockito.when(appUserService.getAppUserByUsername("camerrs"))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches("WrongPassword", "encodedPassword"))
                .thenReturn(false);

        mockMvc.perform(post("/login")
                .param("username", "camerrs")
                .param("password", "WrongPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("passwordsMatch", false));
    }
}
