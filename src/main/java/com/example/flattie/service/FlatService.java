package com.example.flattie.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatExpense;
import com.example.flattie.repository.AppUserRepository;
// import com.example.flattie.repository.FlatExpenseRepository;
import com.example.flattie.repository.FlatRepository;

/**
 * Service class for database interaction with Flat entities.
 * Performs actual calling of FlatRepository methods.
 */
@Service
public class FlatService {

    // @Autowired
    // private FlatExpenseRepository flatExpenseRepository;

    @Autowired
    private AppUserRepository appUserRepository;

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

    /** Chekc if the address already exists in the databse */
    public boolean addressExists(String address) {
        return flatRepository.existsByAddress(address);
    }

    /**
     * Saves a new Flat to the database. this also creates and assigns
     * the rent expense for the flat.
     * 
     * @param flat The Flat to persist in the database.
     * @return The saved entity.
     */
    public Flat saveFlat(Flat flat) {
        // Create a new FlatExpense for the rent
        // Create Rent FlatExpense
        FlatExpense rentExpense = new FlatExpense();
        rentExpense.setFlat(flat);
        rentExpense.setName("Rent");
        // System.out.println("Weekly Rent: " + flat.getWeeklyRent());
        rentExpense.setTotalAmount(BigDecimal.valueOf(flat.getWeeklyRent()));
        // System.out.println("Rent Expense Amount: " + rentExpense.getTotalAmount());
        rentExpense.setExpenseMonth(null); // this is for to support monthly tracking, would use LocalDate.now() for
                                           // this

        // Bind RentExpense to flat
        flat.setRentExpense(rentExpense);

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

    public Flat findByJoinCode(String joinCode) {
        return flatRepository.findByJoinCode(joinCode);
    }

    public Flat findById(Long id) {
        // Use the built-in findById method from JpaRepository
        Optional<Flat> flat = flatRepository.findById(id);
        return flat.orElse(null); // Return the flat if found, otherwise return null
    }

    /**
     * Finds all flatmates associated with a flat by the flat id.
     * 
     * @param id The id of the Flat to find flatmates for.
     * @return A list of Flatmate entities associated with the Flat.
     */
    public List<AppUser> getFlatmates(Long flatId) {
        return appUserRepository.findByFlat_Id(flatId);
    }
}
