package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.ProgressRepository;
import com.ktu.xsports.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ktu.xsports.api.util.Role.MODERATOR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    ProgressRepository progressRepository;

    @Test
    void findUsersReturnsUsers() {
        Pageable pageable = PageRequest.of(1, 20);

        Page<User> expected = new PageImpl<>(List.of(
            testUser()
        ));

        Mockito.when(userRepository.findAll(pageable))
            .thenReturn(expected);

        Page<User> actual = userService.findUsers(pageable, null);

        assertEquals(expected, actual);
    }

    @Test
    void testFindUsers() {
        Pageable pageable = PageRequest.of(1, 20);

        Page<User> expected = new PageImpl<>(List.of(
            testUser()
        ));

        Mockito.when(userRepository.findByUsernameContaining(any(), any()))
            .thenReturn(expected);

        Page<User> actual = userService.findUsers(pageable, "tes");

        assertEquals(expected, actual);
    }

    @Test
    void findById() {
        User expected = testUser();

        Mockito.when(userRepository.findById(any()))
            .thenReturn(Optional.ofNullable(expected));

        User actual = userService.findById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void findByEmail() {
        User expected = testUser();

        Mockito.when(userRepository.findByEmail(any()))
            .thenReturn(Optional.ofNullable(expected));

        User actual = userService.findByEmail("tester@gmail.com");

        assertEquals(expected, actual);
    }

    @Test
    void saveModeratorUser() {
        User expected = testUser();

        Mockito.when(userRepository.save(any()))
            .thenReturn(expected);

        User actual = userService.saveModeratorUser(expected);

        assertEquals(expected, actual);
    }

    @Test
    void updateUserById() {
        User expected = testUser();

        Mockito.when(userRepository.save(any()))
            .thenReturn(expected);

        Mockito.when(userRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Optional<User> actual = userService.updateUserById(expected, 1);

        assertEquals(expected, actual.get());
    }

    @Test
    void removeUser() {
        User expected = testUser();

        Mockito.when(userRepository.save(any()))
            .thenReturn(expected);

        Mockito.when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(expected));

        Mockito.when(progressRepository.findProgressByUser(anyLong()))
            .thenReturn(List.of());

        Mockito.doNothing().when(progressRepository).deleteAll(List.of());
        Mockito.doNothing().when(userRepository).delete(expected);

        User actual = userService.removeUser(1L);

        assertEquals(expected, actual);
    }

    @Test
    void removeSportFromUserList() {
        Sport sport = Sport.builder().build();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(userWithSport(sport)));
        Mockito.when(userRepository.save(any())).thenReturn(userWithSport(sport));

        userService.removeSportFromUserList(sport, 1L);
    }

    @Test
    void removeSportsFromAllUsers() {
        Sport sport = Sport.builder().build();

        Mockito.when(userRepository.findAll())
            .thenReturn(List.of(userWithSport(sport)));

        userService.removeSportsFromAllUsers(sport);
    }

    @Test
    void removeSportFromModerators() {
        Sport sport = Sport.builder().build();

        Mockito.when(userRepository.findAllModerators())
            .thenReturn(List.of(userWithSport(sport)));

        userService.removeSportFromModerators(Sport.builder().build());
    }

    private User testUser() {
        return User.builder()
            .name("test")
            .photoUrl("test")
            .email("test@email.com")
            .surname("test")
            .id(1)
            .surname("testing")
            .isBlocked(false)
            .nickname("tester")
            .role(MODERATOR)
            .sports(new ArrayList<>())
            .build();
    }

    private User userWithSport(Sport sport) {
        User user = testUser();
        user.getSports().add(sport);
        return user;
    }
}