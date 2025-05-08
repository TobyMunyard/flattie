package com.example.flattie.controller;

import com.example.flattie.config.TestSecurityConfig;
import com.example.flattie.model.Event;
import com.example.flattie.model.Flat;
import com.example.flattie.repository.EventRepository;
import com.example.flattie.repository.FlatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private FlatRepository flatRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEvents_returnsList() throws Exception {
        LocalDate date = LocalDate.of(2025, 5, 8);
        LocalDate differentDate = LocalDate.of(2025, 9, 22);

        Event e1 = new Event("Event 1", date);
        Event e2 = new Event("Event 2", differentDate);

        when(eventRepository.findAll()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getEventsByFlat_returnsFlatEvents() throws Exception {
        LocalDate date = LocalDate.of(2025, 5, 8);
        Long flatId = 1L;
        Flat flat = new Flat();
        flat.setId(flatId);

        Event event = new Event("Flat Event", date);
        event.setFlat(flat);

        when(flatRepository.findById(flatId)).thenReturn(Optional.of(flat));
        when(eventRepository.findByFlat(flat)).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events/flat/{flatId}", flatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Flat Event"));
    }

    @Test
    void addEvent_savesAndReturnsEvent() throws Exception {
        Long flatId = 1L;
        Flat flat = new Flat();
        flat.setId(flatId);

        Event event = new Event("New Event", LocalDate.now());

        Event savedEvent = new Event("New Event", LocalDate.now());
        savedEvent.setFlat(flat);

        when(flatRepository.findById(flatId)).thenReturn(Optional.of(flat));
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        mockMvc.perform(post("/api/events/flat/{flatId}", flatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Event"));
    }
}
