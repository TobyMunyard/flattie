package com.example.flattie.service;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.Notice;
import com.example.flattie.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveNotice_callsRepositorySave() {
        Notice notice = new Notice("Test", "Testing saving!", new AppUser(), new Flat());

        noticeService.saveNotice(notice);

        verify(noticeRepository).save(notice);
    }

    @Test
    void deleteById_callsRepositoryDeleteById() {
        Long id = 1L;

        noticeService.deleteById(id);

        verify(noticeRepository).deleteById(id);
    }

    @Test
    void getNoticeById_returnsCorrectNotice() {
        Long id = 2L;
        Notice mockNotice = new Notice("Test", "Testing saving!", new AppUser(), new Flat());
        when(noticeRepository.getNoticeById(id)).thenReturn(mockNotice);

        Notice result = noticeService.getNoticeById(id);

        assertThat(result).isSameAs(mockNotice);
        verify(noticeRepository).getNoticeById(id);
    }
}
