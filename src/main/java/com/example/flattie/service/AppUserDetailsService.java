package com.example.flattie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.flattie.model.AppUser;
import com.example.flattie.repository.AppUserRepository;

/**
 * Service class used for managing users within the system. Used by spring
 * security for user actions.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    /**
     * Constructs an instance of AppUserDetailsService using the associated
     * repository.
     * 
     * @param userRepository The AppUserRepository being used.
     */
    @Autowired
    public AppUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user based on their username. Spring security verifies that the
     * password matches before this happens.
     * 
     * @param username The username of the user being retrieved.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.getAppUserByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
