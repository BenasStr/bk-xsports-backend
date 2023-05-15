package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Status;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.ProgressRepository;
import com.ktu.xsports.api.service.trick.TrickGroupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class ProgressServiceTest {

    @Autowired
    ProgressService service;

    @MockBean
    ProgressRepository progressRepository;
    @MockBean
    TrickGroupService trickGroupService;
    @MockBean
    UserService userService;
    @MockBean
    StatusService statusService;

    @Test
    void findProgressByUser() {
        List<Progress> expected = List.of(getTestProgress());

        Mockito.when(progressRepository.findProgressByUser(anyLong()))
                .thenReturn(expected);

        List<Progress> actual = service.findProgressByUser(1);

        assertEquals(expected, actual);
    }

    @Test
    void updateProgressExists() {
        Mockito.when(trickGroupService.findTrickById(anyLong(), anyLong(), anyLong()))
            .thenReturn(TrickVariant.builder().build());

        Mockito.when(userService.findById(anyLong()))
            .thenReturn(User.builder().build());

        Mockito.when(progressRepository.findTrickAndUserId(anyLong(), anyLong()))
            .thenReturn(Optional.ofNullable(Progress.builder().build()));

        TrickVariant actual = service.updateProgress(1, 1, 1, 1);
    }

    private Progress getTestProgress() {
        return Progress.builder()
            .status(new Status())
            .trickVariant(TrickVariant.builder().build())
            .user(User.builder().build())
            .id(1)
            .build();
    }
}