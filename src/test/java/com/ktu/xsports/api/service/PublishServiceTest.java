package com.ktu.xsports.api.service;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.repository.PublishRepository;
import com.ktu.xsports.api.service.trick.TrickGroupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class PublishServiceTest {

    @Autowired
    PublishService service;

    @MockBean
    PublishRepository publishRepository;
    @MockBean
    SportService sportService;
    @MockBean
    CategoryService categoryService;
    @MockBean
    TrickGroupService trickGroupService;

    @Test
    void findById() {
        Publish expected = getTestPublish();

        Mockito.when(publishRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(expected));

        Publish actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void findByIdThrowsError() {
        Mockito.when(publishRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> service.findById(1));
    }

    @Test
    void getPublishes() {
        List<Publish> expected = List.of(getTestPublish());
        Mockito.when(publishRepository.findAll())
            .thenReturn(expected);

        List<Publish> actual = service.getPublishes();
        assertEquals(expected, actual);
    }

    @Test
    void createPublish() {
        Publish expected = getTestPublish();

        Mockito.when(publishRepository.save(any()))
            .thenReturn(expected);

        Publish actual = service.createPublish(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updatePublish() {
        Publish expected = getTestPublish();

        Mockito.when(publishRepository.save(any()))
            .thenReturn(expected);

        Mockito.when(publishRepository.findById(anyLong()))
            .thenReturn(Optional.ofNullable(expected));

        Publish actual = service.updatePublish(expected, 1);
        assertEquals(expected, actual);
    }

    @Test
    void deletePublish() {
        Publish expected = getTestPublish();

        Mockito.when(publishRepository.findById(anyLong()))
            .thenReturn(Optional.ofNullable(expected));

        Mockito.doNothing().when(publishRepository)
            .delete(any());

        service.deletePublish(1);
    }

    @Test
    void publish() {
        Publish expected = getTestPublish();

        Mockito.when(publishRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        Mockito.doNothing().when(sportService).publish(any());

        Mockito.doNothing().when(categoryService).publish(any());

        Mockito.doNothing().when(trickGroupService).publish(any());

        service.publish(1);
    }

    @Test
    void publishByCategory() {
        Mockito.when(categoryService.findCategory(anyLong(), anyLong()))
            .thenReturn(getTestPublish().getCategory());

        Mockito.doNothing().when(sportService).publish(any());

        Mockito.doNothing().when(categoryService).publish(any());

        Mockito.doNothing().when(trickGroupService).publish(any());

        service.publishByCategory(1, 1);
    }

    private Publish getTestPublish() {
        return Publish.builder()
            .name("test")
            .category(
                Category.builder()
                    .name("testCateg")
                    .sport(
                        Sport.builder()
                            .name("sporten")
                            .build()
                    ).tricks(List.of())
                    .build())
            .id(1)
            .build();
    }
}