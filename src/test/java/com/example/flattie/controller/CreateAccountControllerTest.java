package com.example.flattie.controller;

import com.example.flattie.config.TestSecurityConfig;
import com.example.flattie.model.AppUser;
import com.example.flattie.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(CreateAccountController.class)
public class CreateAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Reset mocks before each test
        reset(passwordEncoder, appUserService);
    }

    @Test
    public void testValidAccountCreation() throws Exception {
        // Arrange
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("validPassword")).thenReturn(encodedPassword);

        // Act & Assert
        mockMvc.perform(post("/createAccount")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("username", "johnny123")
                .param("password", "validPassword")
                .param("confirmPassword", "validPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Verify
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserService, times(1)).saveAppUser(userCaptor.capture());
        
        AppUser savedUser = userCaptor.getValue();
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("johnny123", savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
        
        // Verify password encoding was called exactly once
        verify(passwordEncoder, times(1)).encode("validPassword");
    }

    
}