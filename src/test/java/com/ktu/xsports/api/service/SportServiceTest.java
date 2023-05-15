package com.ktu.xsports.api.service;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.SportRepository;
import com.ktu.xsports.api.repository.UserRepository;
import com.ktu.xsports.api.service.media.ImageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ktu.xsports.api.util.PublishStatus.DELETED;
import static com.ktu.xsports.api.util.PublishStatus.NOT_PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;
import static com.ktu.xsports.api.util.Role.ADMIN;
import static com.ktu.xsports.api.util.Role.MODERATOR;
import static com.ktu.xsports.api.util.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class SportServiceTest {

    @Autowired
    SportService service;

    @MockBean
    ImageService imageService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    SportRepository sportRepository;

    @MockBean
    UserService userService;

    @Test
    void findAll() {
        List<Sport> expected = List.of(getTestSport());

        Mockito.when(sportRepository.findAll())
                .thenReturn(expected);

        List<Sport> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void findSportsWhenUser() {
        List<Sport> expected = List.of(getTestSport());

        Mockito.when(sportRepository.findAll(any(Specification.class)))
            .thenReturn(expected);

        List<Sport> actual = service.findSports(null, PUBLISHED, User.builder().role(USER).build());
        assertEquals(expected, actual);
    }

    @Test
    void findSportsWhenModerator() {
        List<Sport> expected = List.of(getTestSport());

        Mockito.when(sportRepository.findAll(any(Specification.class)))
            .thenReturn(expected);

        List<Sport> actual = service.findSports(null, PUBLISHED, User.builder().role(MODERATOR).build());
        assertEquals(expected, actual);
    }

    @Test
    void findMySportsADMIN() {
        List<Sport> expected = List.of(getTestSport());

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(User.builder().sports(expected).role(ADMIN).build());

        List<Sport> actual = service.findMySports(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findMySportsUSER() {
        List<Sport> expected = List.of(getTestSport());

        Mockito.when(userService.findById(anyLong()))
            .thenReturn(
                User.builder()
                .sports(expected)
                .role(USER)
                .build());

        List<Sport> actual = service.findMySports(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findExploreSportsUSER() {
        List<Sport> expected = List.of(getTestSport());

        Mockito.when(userService.findById(anyLong()))
            .thenReturn(
                User.builder()
                    .sports(expected)
                    .role(USER)
                    .build());

        Mockito.when(sportRepository.findAll())
            .thenReturn(expected);

        List<Sport> actual = service.findExploreSports(1L);
        assertEquals(List.of(), actual);
    }

    @Test
    void findExploreSportsADMIN() {
        List<Sport> expected = List.of(getTestSport());

        Mockito.when(userService.findById(anyLong()))
            .thenReturn(
                User.builder()
                    .sports(expected)
                    .role(ADMIN)
                    .build());

        Mockito.when(sportRepository.findAll())
            .thenReturn(expected);

        List<Sport> actual = service.findExploreSports(1L);
        assertEquals(List.of(), actual);
    }

    @Test
    void addSportToUserListContains() {
        Sport sport = getTestSport();
        List<Sport> expected = List.of(sport);

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(sport));

        Mockito.when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(User.builder().sports(expected).build()));

        Mockito.when(userRepository.save(any()))
            .thenReturn(null);

        service.addSportToUserList(1, "email@gmail.com");
    }

    @Test
    void addSportToUserListDoesNotContain() {
        Sport sport = getTestSport();
        List<Sport> expected = new ArrayList<>();

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(sport));

        Mockito.when(userRepository.findByEmail(any()))
            .thenReturn(Optional.of(User.builder().sports(expected).build()));

        Mockito.when(userRepository.save(any()))
            .thenReturn(null);

        service.addSportToUserList(1, "email@gmail.com");
    }

    @Test
    void findSportById() {
        Sport expected = getTestSport();
        Mockito.when(sportRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(expected));
        Sport actual = service.findSportById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findSportByIdThrowsException() {
        Mockito.when(sportRepository.findById(anyLong()))
            .thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> service.findSportById(1L));
    }

    @Test
    void createSport() {
        Sport expected = getTestSport();

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Sport actual = service.createSport(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateSportPublished() {
        Sport expected = getTestSport();

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.ofNullable(expected));

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Sport actual = service.updateSport(expected, 1);
        assertEquals(expected, actual);
    }

    @Test
    void updateSportUpdated() {
        Sport expected = getTestSport();
        expected.setPublishStatus(NOT_PUBLISHED);

        expected.setUpdatedBy(getUpdatedByTestSport());

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Sport actual = service.updateSport(expected, 1);
        assertEquals(expected, actual);
    }

    @Test
    void updateSportNotPublished() {
        Sport expected = getTestSport();

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Sport actual = service.updateSport(expected, 1);
        assertEquals(expected, actual);
    }

    @Test
    void updateSportImage() {
        Sport expected = getTestSport();

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Mockito.when(imageService.updateImage(any(), any()))
            .thenReturn("updatedURL");

        Mockito.when(imageService.uploadImage(any(), any()))
            .thenReturn("uploadedURL");

        Sport actual = service.updateSportImage(1, null);
        expected.setPhotoUrl("updatedURL");

        assertEquals(expected.getPhotoUrl(), actual.getPhotoUrl());
    }

    @Test
    void updateSportImageWhenItDoesNotExist() {
        Sport expected = getTestSport();
        expected.setPhotoUrl(null);

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Mockito.when(imageService.updateImage(any(), any()))
            .thenReturn("updatedURL");

        Mockito.when(imageService.uploadImage(any(), any()))
            .thenReturn("uploadedURL");

        Sport actual = service.updateSportImage(1, null);
        expected.setPhotoUrl("uploadedURL");

        assertEquals(expected.getPhotoUrl(), actual.getPhotoUrl());
    }

    @Test
    void removeSportPublished() {
        Sport expected = getTestSport();

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Mockito.doNothing().when(imageService).deleteImage(any());

        Mockito.doNothing().when(userService).removeSportsFromAllUsers(any());
        Mockito.doNothing().when(userService).removeSportFromModerators(any());

        Mockito.doNothing().when(sportRepository).deleteById(anyLong());

        service.removeSport(1);
    }

    @Test
    void removeSportUpdated() {
        Sport expected = getTestSport();
        expected.setUpdatedBy(getUpdatedByTestSport());

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Mockito.doNothing().when(imageService).deleteImage(any());

        Mockito.doNothing().when(userService).removeSportsFromAllUsers(any());
        Mockito.doNothing().when(userService).removeSportFromModerators(any());

        Mockito.doNothing().when(sportRepository).deleteById(anyLong());

        service.removeSport(1);
    }

    @Test
    void removeSportNotPublished() {
        Sport expected = getTestSport();
        expected.setPublishStatus(NOT_PUBLISHED);

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        Mockito.doNothing().when(imageService).deleteImage(any());

        Mockito.doNothing().when(userService).removeSportsFromAllUsers(any());
        Mockito.doNothing().when(userService).removeSportFromModerators(any());

        Mockito.doNothing().when(sportRepository).deleteById(anyLong());

        service.removeSport(1);
    }

    @Test
    void removeMyListSport() {
        Sport expected = getTestSport();
        expected.setPublishStatus(NOT_PUBLISHED);

        Mockito.when(userRepository.findById(any()))
            .thenReturn(Optional.ofNullable(User.builder().build()));

        Mockito.when(userRepository.save(any()))
                .thenReturn(User.builder().build());

        Mockito.when(sportRepository.findById(any()))
            .thenReturn(Optional.of(expected));

        service.removeMyListSport(1, 1);
    }

    @Test
    void removeMyListSportThrowsException() {
        Sport expected = getTestSport();
        expected.setPublishStatus(NOT_PUBLISHED);

        Mockito.when(userRepository.findById(any()))
            .thenReturn(Optional.ofNullable(User.builder().build()));

        Mockito.when(userRepository.save(any()))
            .thenReturn(User.builder().build());

        Mockito.when(sportRepository.findById(any()))
            .thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> service.removeMyListSport(1, 1));
    }

    @Test
    void publishPublished() {
        Sport expected = getTestSport();
        expected.setUpdatedBy(getUpdatedByTestSport());

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Mockito.doNothing().when(sportRepository)
            .deleteById(anyLong());

        Mockito.doNothing().when(imageService)
            .deleteImage(any());

        Mockito.when(sportRepository.findById(any()))
                .thenReturn(Optional.of(expected));

        service.publish(expected);
    }

    @Test
    void publishNotPublished() {
        Sport expected = getTestSport();
        expected.setPublishStatus(NOT_PUBLISHED);

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Mockito.doNothing().when(sportRepository)
            .deleteById(anyLong());

        Mockito.doNothing().when(imageService)
            .deleteImage(any());

        service.publish(expected);
    }

    @Test
    void publishDeleted() {
        Sport expected = getTestSport();
        expected.setPublishStatus(DELETED);

        Mockito.when(sportRepository.save(any()))
            .thenReturn(expected);

        Mockito.doNothing().when(sportRepository)
            .deleteById(anyLong());

        Mockito.doNothing().when(imageService)
            .deleteImage(any());

        service.publish(expected);
    }

    private Sport getTestSport() {
        return Sport.builder()
            .name("test")
            .id(1)
            .variants(List.of())
            .categories(List.of())
            .publishStatus(PUBLISHED)
            .photoUrl("testURL")
            .updatedBy(null)
            .build();
    }

    private Sport getUpdatedByTestSport() {
        return Sport.builder()
            .name("as")
            .id(2)
            .variants(List.of())
            .categories(List.of())
            .publishStatus(UPDATED)
            .photoUrl("testURLas")
            .updatedBy(null)
            .build();
    }
}