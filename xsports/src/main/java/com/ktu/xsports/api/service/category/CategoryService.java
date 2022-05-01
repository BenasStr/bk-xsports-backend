package com.ktu.xsports.api.service.category;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.repository.CategoryRepository;
import com.ktu.xsports.api.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SportRepository sportRepository;

    public List<Category> findCategories(long sportId) {
        return categoryRepository.findAllBySportId(sportId);
    }

    public Optional<Category> findCategory(long sportId, long categoryId) {
        return categoryRepository.findBySportIdAndId(sportId, categoryId);
}

    public Optional<Category> createCategory(long sportId, Category category) {
        Optional<Sport> sport = sportRepository.findById(sportId);
        if (sport.isPresent()) {
            category.setSport(sport.get());
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }

    public Optional<Category> updateCategory(long sportId, Category category, long id) {
        category.setId(id);
        if(categoryRepository.findBySportIdAndId(sportId, id).isPresent()) {
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }

    public Optional<Category> removeCategory(long sportId, long id) {
        Optional<Category> deletedCategory = categoryRepository.findBySportIdAndId(sportId, id);
        if(deletedCategory.isPresent()) {
            categoryRepository.delete(deletedCategory.get());
            return deletedCategory;
        }
        return Optional.empty();
    }
}
