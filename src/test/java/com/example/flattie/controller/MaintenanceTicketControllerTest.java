// package com.example.flattie.controller;

// import com.example.flattie.service.MaintenanceTicketService;
// import com.example.flattie.service.ImageService;
// import com.example.flattie.service.EmailService;
// import com.example.flattie.repository.FlatRepository;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Import;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(MaintenanceTicketController.class)
// @Import(MaintenanceTicketControllerTest.NoSecurityConfig.class)
// class MaintenanceTicketControllerTest {

//     @Autowired private MockMvc mockMvc;

//     // Required mocks for controller instantiation
//     @MockBean private MaintenanceTicketService ticketService;
//     @MockBean private ImageService imageService;
//     @MockBean private EmailService emailService;
//     @MockBean private FlatRepository flatRepository;

//     @Test
//     void testConfirmTicketSimple() throws Exception {
//         mockMvc.perform(get("/maintenance/confirm/abc123"))
//             .andExpect(status().isOk())
//             .andExpect(content().string("Got token: abc123"));
//     }

//     // This is just to bypass security for the test
//     @TestConfiguration
//     static class NoSecurityConfig {
//         @Bean
//         public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//             http.csrf().disable().authorizeHttpRequests().anyRequest().permitAll();
//             return http.build();
//         }
//     }
// }
