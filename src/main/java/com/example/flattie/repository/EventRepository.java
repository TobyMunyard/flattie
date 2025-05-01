package com.example.flattie.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.flattie.model.Event;
import com.example.flattie.model.Flat;

/**
 * Repository interface for interacting with events on a flat calendar
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByDate(LocalDate date);
    List<Event> findByFlat(Flat flat);
}