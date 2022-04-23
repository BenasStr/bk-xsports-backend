package com.ktu.xsports.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToCategoryConverter extends StdConverter<Long, Category> {

    private final CategoryRepository categoryRepository;

    @Override
    public Category convert(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
