package com.example.flattie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.flattie.service.AppUserDetailsService;

/**
 * Configuration file for altering spring security configuration.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

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
        http
                .csrf().disable() // Enable this only if you're handling CSRF tokens manually
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/login", "/about", "/viewFlats", "createFlat", "/createAccount", "/h2-console/**", "/css/**",
                                "/js/**")
                        .permitAll()
                        .requestMatchers("/logoutPage", "/shoppingList", "/choreList", "/rentCalculator", "/joinFlat", "/flatInfo").authenticated()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login") // Form POST action
                        .defaultSuccessUrl("/joinFlat", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true) // Ensure session is invalidated on logout
                        .clearAuthentication(true) // Clear the authentication object
                        .permitAll())
                .headers((headers) -> headers
                        .frameOptions().disable() // Makes H2 page usable DO NOT REMOVE
                )
                .anonymous().disable(); // DO NOT REMOVE, STOPS SPRING FROM AUTHENTICATING BY DEFAULT

        return http.build();
    }

    /**
     * Decoupled password encoder to prevent accidental use of different encoders
     * that may lead to issues when attempting to verify if a user's password is
     * correct.
     * 
     * @return A BCryptPasswordEncoder for hashing user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
