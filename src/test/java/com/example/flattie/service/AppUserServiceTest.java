package com.example.flattie.service;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.repository.AppUserRepository;
import com.example.flattie.repository.FlatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private FlatRepository flatRepository;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(appUserService, "flatRepository", flatRepository); // Done because of isolation
                                                                                        // from flatRepository. This
                                                                                        // mocks it individually.
    }

    @Test
    void getAllAppUsers_returnsUserList() {
        List<AppUser> mockUsers = List.of(new AppUser(), new AppUser());
        when(appUserRepository.findAll()).thenReturn(mockUsers);

        List<AppUser> result = appUserService.getAllAppUsers();

        assertThat(result).hasSize(2);
        verify(appUserRepository).findAll();
    }

    @Test
    void saveAppUser_savesAndReturnsUser() {
        AppUser user = new AppUser();
        when(appUserRepository.save(user)).thenReturn(user);

        AppUser result = appUserService.saveAppUser(user);

        assertThat(result).isSameAs(user);
        verify(appUserRepository).save(user);
    }

    @Test
    void deleteAppUser_deletesById() {
        Long userId = 1L;

        appUserService.deleteAppUser(userId);

        verify(appUserRepository).deleteById(userId);
    }

    @Test
    void getAppUserByUsername_returnsOptionalUser() {
        String username = "john";
        AppUser user = new AppUser();
        when(appUserRepository.getAppUserByUsername(username)).thenReturn(Optional.of(user));

        Optional<AppUser> result = appUserService.getAppUserByUsername(username);

        assertThat(result).isPresent();
        verify(appUserRepository).getAppUserByUsername(username);
    }

    @Test
    void getAppUserById_returnsOptionalUser() {
        Long id = 2L;
        AppUser user = new AppUser();
        when(appUserRepository.getAppUserById(id)).thenReturn(Optional.of(user));

        Optional<AppUser> result = appUserService.getAppUserById(id);

        assertThat(result).isPresent();
        verify(appUserRepository).getAppUserById(id);
    }

    @Test
    void joinFlat_setsFlatAndSavesUser() {
        AppUser user = new AppUser();
        Flat flat = new Flat();

        appUserService.joinFlat(user, flat);

        assertThat(user.getFlat()).isEqualTo(flat);
        verify(appUserRepository).save(user);
    }

    @Test
    void getFlatsForUser_returnsSingleFlatInList() {
        Flat flat = new Flat();
        AppUser user = new AppUser();
        user.setFlat(flat);

        List<Flat> result = appUserService.getFlatsForUser(user);

        assertThat(result).containsExactly(flat);
    }

    @Test
    void saveFlat_callsFlatRepoSave() {
        Flat flat = new Flat();

        appUserService.saveFlat(flat);

        verify(flatRepository).save(flat);
    }
}
