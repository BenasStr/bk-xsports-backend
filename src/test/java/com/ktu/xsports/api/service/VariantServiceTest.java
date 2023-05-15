package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.repository.VariantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class VariantServiceTest {

    @Autowired
    VariantService service;

    @MockBean
    VariantRepository repository;

    @Test
    void getVariantsReturnsVariants() {
        List<Variant> expected = List.of(
            Variant.builder()
                .name("test")
                .id(1)
                .sports(List.of())
                .build()
        );

        Mockito.when(repository.findAll())
            .thenReturn(expected);

        List<Variant> actual = service.getVariants();

        assertEquals(expected, actual);
    }

    @Test
    void getStandardVariantReturnsStandardVariant() {
        Variant expected = Variant.builder()
            .name("test")
            .id(1)
            .sports(List.of())
            .build();

        Mockito.when(repository.findByName("Standard"))
            .thenReturn(Optional.of(expected));

        Variant actual = service.getStandardVariant();

        assertEquals(expected, actual);
    }

    @Test
    void createVariantCreatesVariant() {
        Variant expected = Variant.builder()
            .name("test")
            .id(1)
            .sports(List.of())
            .build();

        Mockito.when(repository.save(expected))
            .thenReturn(expected);

        Variant actual = service.createVariant(expected);

        assertEquals(expected, actual);
    }

    @Test
    void updateVariantCreatesVariant() {
        Variant expected = Variant.builder()
            .name("test")
            .id(1)
            .sports(List.of())
            .build();

        Mockito.when(repository.save(any()))
            .thenReturn(expected);

        Mockito.when(repository.findById(any()))
            .thenReturn(Optional.of(expected));

        Variant actual = service.updateVariant(expected, 1L);

        assertEquals(expected, actual);
    }

    @Test
    void deleteVariantCreatesVariant() {
        Variant expected = Variant.builder()
            .name("test")
            .id(1)
            .sports(List.of())
            .trickVariants(List.of())
            .build();

        Mockito.when(repository.save(any()))
            .thenReturn(expected);

        Mockito.when(repository.findById(any()))
            .thenReturn(Optional.of(expected));

        service.deleteVariant(1L);
    }
}