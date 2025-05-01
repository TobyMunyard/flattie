package com.example.flattie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.flattie.model.Event;
import com.example.flattie.model.Flat;
import com.example.flattie.repository.EventRepository;
import com.example.flattie.repository.FlatRepository;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FlatRepository flatRepository;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/flat/{flatId}")
    public List<Event> getEventsByFlat(@PathVariable("flatId") Long flatId) {
        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        return eventRepository.findByFlat(flat);
    }

    @PostMapping("/flat/{flatId}")
    public Event addEvent(@PathVariable("flatId") Long flatId, @RequestBody Event event) {
        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));

        event.setFlat(flat);
        return eventRepository.save(event);
    }
}
