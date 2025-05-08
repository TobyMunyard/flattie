package com.example.flattie.repository;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Test
    @DisplayName("Should save and retrieve user by ID")
    void testGetAppUserById() {
        AppUser user = new AppUser("john", "doe", "testuser", "password");
        AppUser savedUser = appUserRepository.save(user);

        Optional<AppUser> result = appUserRepository.getAppUserById(savedUser.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should retrieve user by username")
    void testGetAppUserByUsername() {
        AppUser user = new AppUser("john", "doe", "johndoe", "password");
        appUserRepository.save(user);

        Optional<AppUser> result = appUserRepository.getAppUserByUsername("johndoe");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("Should find users by flat ID")
    void testFindByFlatId() {
        Flat flat = new Flat("123", "Castle Street Flat", "123 Castle Street", "Dunedin", "9016", "A dirty flat!", 680, 5);
        flat = flatRepository.save(flat);

        AppUser user1 = new AppUser("alice", "doe", "aliceFlat", "password");
        AppUser user2 = new AppUser("bob", "doe", "bobbyBoy", "testPassword");

        user1.setFlat(flat);
        user2.setFlat(flat);

        appUserRepository.saveAll(List.of(user1, user2));

        List<AppUser> usersInFlat = appUserRepository.findByFlat_Id(flat.getId());

        assertThat(usersInFlat).hasSize(2);
        assertThat(usersInFlat).extracting(AppUser::getUsername).containsExactlyInAnyOrder("aliceFlat", "bobbyBoy");
    }
}
