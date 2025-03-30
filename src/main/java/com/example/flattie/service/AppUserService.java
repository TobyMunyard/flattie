package com.example.flattie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.flattie.model.AppUser;
import com.example.flattie.repository.AppUserRepository;

/**
 * Service class for database interaction with AppUser entities.
 * Performs actual calling of AppUserRepository methods.
 */
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
 
    /**
     * Gets all users currently in the database.
     * 
     * @return A list of all current AppUser entities in the database.
     */
    public List<AppUser> getAllAppUsers() {
        return appUserRepository.findAll();
    }

    /**
     * Saves a new AppUser to the database.
     * 
     * @param user The AppUser to persist in the database.
     * @return The saved entity.
     */
    public AppUser saveAppUser(AppUser user) {
        return appUserRepository.save(user);
    }

    /**
     * Deletes a AppUser from the databse based on a provided id.
     * 
     * @param id The id of the AppUser to delete from the database.
     */
    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> getAppUserByUsername(String username){
        return appUserRepository.getAppUserByUsername(username);
    }
}
