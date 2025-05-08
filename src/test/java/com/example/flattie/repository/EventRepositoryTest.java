package com.example.flattie.repository;

import com.example.flattie.model.Event;
import com.example.flattie.model.Flat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventRepositoryTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventRepositoryTest eventRepositoryTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllByDate_returnsEventList() {
        LocalDate date = LocalDate.of(2025, 5, 8);
        Event event = new Event("John's Birthday", date);

        List<Event> mockEvents = List.of(event);
        when(eventRepository.findAllByDate(date)).thenReturn(mockEvents);

        List<Event> result = eventRepository.findAllByDate(date);

        assertThat(result).hasSize(1);
        verify(eventRepository).findAllByDate(date);
    }

    @Test
    void findByFlat_returnsEventList() {
        LocalDate date = LocalDate.of(2025, 5, 8);
        Flat flat = new Flat();
        Event event = new Event("Alice's Birthday", date);
        List<Event> mockEvents = List.of(event);
        when(eventRepository.findByFlat(flat)).thenReturn(mockEvents);

        List<Event> result = eventRepository.findByFlat(flat);

        assertThat(result).hasSize(1);
        verify(eventRepository).findByFlat(flat);
    }
}
