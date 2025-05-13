package com.example.flattie.service;

import com.example.flattie.model.Flat;
import com.example.flattie.model.PropertyManager;
import com.example.flattie.repository.FlatRepository;
import com.example.flattie.repository.PropertyManagerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FlatServiceTest {

    private FlatRepository flatRepository;
    private PropertyManagerRepository propertyManagerRepository;
    private FlatService flatService;

    @BeforeEach
    public void setUp() {
        flatRepository = mock(FlatRepository.class);
        propertyManagerRepository = mock(PropertyManagerRepository.class);
        flatService = new FlatService(flatRepository, propertyManagerRepository);
    }

    @Test
    public void testAssignPropertyManager() {
        Flat flat = new Flat();
        flat.setId(1L);

        String pmName = "Darryl PM";
        String pmEmail = "pm@example.com";
        String pmPhone = "123456789";

        when(propertyManagerRepository.findByEmail(pmEmail)).thenReturn(Optional.empty());

        flatService.assignPropertyManager(flat, pmName, pmEmail, pmPhone);

        ArgumentCaptor<Flat> flatCaptor = ArgumentCaptor.forClass(Flat.class);
        verify(flatRepository).save(flatCaptor.capture());

        Flat savedFlat = flatCaptor.getValue();
        PropertyManager assigned = savedFlat.getPropertyManager();

        assertEquals(pmName, assigned.getName());
        assertEquals(pmEmail, assigned.getEmail());
        assertEquals(pmPhone, assigned.getPhone());
    }

    @Test
    public void testReusesExistingPropertyManager() {
        // Existing PM setup
        PropertyManager existingPm = new PropertyManager();
        existingPm.setName("Existing PM");
        existingPm.setEmail("pm@example.com");
        existingPm.setPhone("123456789");

        when(propertyManagerRepository.findByEmail("pm@example.com"))
                .thenReturn(Optional.of(existingPm));

        Flat newFlat = new Flat();
        newFlat.setId(2L);

        flatService.assignPropertyManager(newFlat, "Should Be Ignored", "pm@example.com", "999");

        ArgumentCaptor<Flat> flatCaptor = ArgumentCaptor.forClass(Flat.class);
        verify(flatRepository).save(flatCaptor.capture());

        Flat savedFlat = flatCaptor.getValue();

        // confirm the existing PM was reused
        assertSame(existingPm, savedFlat.getPropertyManager());
        assertEquals("pm@example.com", savedFlat.getPropertyManager().getEmail());
        assertEquals("Existing PM", savedFlat.getPropertyManager().getName());
        assertEquals("123456789", savedFlat.getPropertyManager().getPhone());

        // confirm the flat was added to existing PM’s flat list
        assertTrue(existingPm.getFlats().contains(savedFlat));
    }

}
