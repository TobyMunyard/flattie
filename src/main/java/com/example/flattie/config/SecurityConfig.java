package com.example.flattie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration file for altering spring security configuration.
 */
@Configuration
public class SecurityConfig {

    /**
     * Allows for the alteration and configuration of the security settings for the
     * web application.
     * 
     * @param http Injected by Spring, used to configure the security settings of
     *             the web application.
     * @return The configured HttpSecurity object.
     * @throws Exception If something goes wrong when configuring security settings.
     */
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // DO NOT REMOVE THESE TWO LINES THEY ALLOW H2 TO NOT BE BLOCKED!
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
                .csrf(csrf -> csrf.disable()) // Disable CSRF (for development)
                .formLogin(login -> login.disable()) // Disable login form
                .httpBasic(basic -> basic.disable()); // Disable basic auth

        return http.build();
    }

    /**
     * Decoupled password encoder to prevent accidental use of different encoders
     * that may lead to issues when attempting to verify if a user's password is
     * correct.
     * 
     * @return A BCryptPasswordEncoder for hashing user passwords.
     */
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
