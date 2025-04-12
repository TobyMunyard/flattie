package com.example.flattie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.repository.AppUserRepository;
import com.example.flattie.repository.FlatRepository;

/**
 * Service class for database interaction with AppUser entities.
 * Performs actual calling of AppUserRepository methods.
 */
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Autowired
    private FlatRepository flatRepository; // Inject FlatRepository

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
     * Deletes a AppUser from the database based on a provided id.
     * 
     * @param id The id of the AppUser to delete from the database.
     */
    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> getAppUserByUsername(String username) {
        return appUserRepository.getAppUserByUsername(username);
    }

    /**
     * Adds a user to a flat. This method is called when a user joins a flat.
     * It sets the flat in the user's database record and saves the updated user.
     * 
     * @param user The user to be added to the flat.
     * @param flat The flat to which the user is being added.
     */
    public void joinFlat(AppUser user, Flat flat) {
        // Set the flat in the user's record
        user.setFlat(flat);
        // Save the updated user
        appUserRepository.save(user);
    }

    // TODO: do we actually need this? its assumed that a user can only be in one
    // flat at a time
    public List<Flat> getFlatsForUser(AppUser user) {
        // Assuming AppUser has a relationship with Flat (e.g., @ManyToMany or
        // @OneToMany)
        List<Flat> flats = new ArrayList<>();
        flats.add(user.getFlat());
        return flats;
    }

    public void saveFlat(Flat flat) {
        flatRepository.save(flat);
    }
}
