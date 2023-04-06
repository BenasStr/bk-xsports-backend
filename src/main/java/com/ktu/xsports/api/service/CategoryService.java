package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.exceptions.AlreadyExistsException;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.CategoryRepository;
import com.ktu.xsports.api.service.media.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;
    private final SportService sportService;

    public List<Category> findCategories(long sportId) {
        return categoryRepository.findAllBySportId(sportId);
    }

    public List<Category> findCategories(long sportId, String search) {
        return search == null || search.equals("") ?
            categoryRepository.findAllBySportId(sportId) :
            categoryRepository.findByNameContaining(sportId, search);
    }

    public Category findCategory(long sportId, long categoryId) {
        return categoryRepository.findBySportIdAndId(sportId, categoryId)
            .orElseThrow(() -> new ServiceException("Category doesn't exist"));
}

    public Category createCategory(long sportId, Category category) {
        Sport sport = sportService.findSportById(sportId);

        Optional<Category> categoryExists = categoryRepository.findCategoryWithName(category.getName(), sportId);
        if (categoryExists.isPresent()) {
            throw new AlreadyExistsException(String.format("Category with name %s already exists.", category.getName()));
        }

        category.setSport(sport);
        return categoryRepository.save(category);
    }

    public Category updateCategory(long sportId, Category category, long categoryId) {
       Sport sport = sportService.findSportById(sportId);

        Optional<Category> categoryExists = categoryRepository.findCategoryWithName(category.getName(), sportId, categoryId);
        if (categoryExists.isPresent()) {
            throw new AlreadyExistsException(String.format("Category with name %s already exists.", category.getName()));
        }

        category.setId(categoryId);
        category.setSport(sport);
        categoryRepository.findBySportIdAndId(sportId, categoryId)
            .orElseThrow(() -> new ServiceException("Category doesn't exist"));
        return categoryRepository.save(category);
    }

    public void removeCategory(long sportId, long id) {
        categoryRepository.findBySportIdAndId(sportId, id)
            .ifPresent(category -> {
                if (category.getPhotoUrl() != null) {
                    imageService.deleteImage(category.getPhotoUrl());
                }
            });

        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(String.format("Category doesn't exist: %d", id));
        }
    }
}
