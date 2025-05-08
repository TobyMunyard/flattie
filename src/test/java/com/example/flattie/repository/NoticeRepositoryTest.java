package com.example.flattie.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.Notice;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Test
    @DisplayName("Should get a notice by ID")
    void testGetAppUserById() {
        AppUser user = new AppUser("john", "doe", "testuser", "password");
        Flat flat = new Flat("123", "Castle Street Flat", "123 Castle Street", "Dunedin", "9016", "A dirty flat!", 680, 5);
        Notice notice = new Notice("Flooding", "There is flooding in the kitchen!", user, flat);
        appUserRepository.save(user);
        flatRepository.save(flat);
        noticeRepository.save(notice);

        Notice result = noticeRepository.getNoticeById(notice.getId());

        assertThat(result.getTitle()).isEqualTo("Flooding");
        assertThat(result.getDescription()).isEqualTo("There is flooding in the kitchen!");
        assertThat(result.getCreator()).isEqualTo(user);
        assertThat(result.getFlat()).isEqualTo(flat);
    }
}
