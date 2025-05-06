package com.example.flattie.controller;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatMembership;
import com.example.flattie.model.FlatMembershipStatus;
import com.example.flattie.model.Role;
import com.example.flattie.repository.FlatRepository;
import com.example.flattie.service.AppUserService;
import com.example.flattie.service.FlatMembershipService;
import com.example.flattie.service.FlatService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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

        @MockBean
        private FlatService flatService;

        @MockBean
        private FlatMembershipService flatMembershipService;

        @MockBean
        private FlatRepository flatRepository;

        @Test
        void testLoginWithValidCredentials() throws Exception {
                AppUser user = new AppUser("Cam", "Clark", "camerrs", "encodedPassword");
                Flat mockFlat = new Flat("", "TestFlat", "Addr", "City", "PC", "Desc", 0, 0);
                mockFlat.setId(42L);
                user.setFlat(mockFlat);

                FlatMembership membership = new FlatMembership();
                membership.setFlat(mockFlat);
                membership.setUser(user);
                membership.setRole(Role.OWNER);
                membership.setStatus(FlatMembershipStatus.APPROVED);

                when(appUserService.getAppUserByUsername("camerrs")).thenReturn(Optional.of(user));
                when(passwordEncoder.matches("ThisIsASecurePassword", "encodedPassword")).thenReturn(true);
                when(flatRepository.findById(42L)).thenReturn(Optional.of(mockFlat));
                when(flatMembershipService.getMembership(eq(mockFlat), eq(user))).thenReturn(Optional.of(membership));

                mockMvc.perform(post("/login")
                                .param("username", "camerrs")
                                .param("password", "ThisIsASecurePassword"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/showFlatInfo"));
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
