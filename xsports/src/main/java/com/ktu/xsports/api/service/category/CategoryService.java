package com.ktu.xsports.api.service.category;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.repository.CategoryRepository;
import com.ktu.xsports.api.service.category.internal.CategoryCreator;
import com.ktu.xsports.api.service.category.internal.CategoryRemover;
import com.ktu.xsports.api.service.category.internal.CategoryRetriever;
import com.ktu.xsports.api.service.category.internal.CategoryUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryRetriever, CategoryCreator, CategoryUpdater, CategoryRemover {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findCategories(long sportId) {
        return categoryRepository.findAllBySportId(sportId);
    }

    @Override
    public Optional<Category> findCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Optional<Category> createCategory(Category category) {
        return Optional.of(categoryRepository.save(category));
    }

    @Override
    public Optional<Category> updateCategory(Category category, long id) {
        category.setId(id);
        if(categoryRepository.findById(id).isPresent()) {
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> removeCategory(long id) {
        Optional<Category> deletedCategory = categoryRepository.findById(id);
        if(deletedCategory.isPresent()) {
            categoryRepository.delete(deletedCategory.get());
            return deletedCategory;
        }
        return Optional.empty();
    }
}
