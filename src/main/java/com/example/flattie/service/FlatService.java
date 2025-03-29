package com.example.flattie.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.flattie.model.Flat;
import com.example.flattie.repository.FlatRepository;

/**
 * Service class for database interaction with User entities.
 * Performs actual calling of UserRepository methods.
 */
@Service
public class FlatService {
    private final FlatRepository flatRepository;

    @Autowired
    public FlatService(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    /**
     * Gets all flats currently in the database.
     * 
     * @return A list of all current Flat entities in the database.
     */
    public List<Flat> getAllFlats() {
        return flatRepository.findAll();
    }

    /**Chekc if the address already exists in the databse */
    public boolean addressExists(String address) {
        return flatRepository.existsByAddress(address);
}

    /**
     * Saves a new Flat to the database.
     * 
     * @param flat The Flat to persist in the database.
     * @return The saved entity.
     */
    public Flat saveFlat(Flat flat) {
        return flatRepository.save(flat);
    }

    /**
     * Deletes a Flat from the databse based on a provided id.
     * 
     * @param id The id of the Flat to delete from the database.
     */
    public void deleteFlat(Long id) {
        flatRepository.deleteById(id);
    }
}
