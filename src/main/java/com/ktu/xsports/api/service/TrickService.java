package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.service.category.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrickService {

    private final TrickRepository trickRepository;
    private final CategoryService categoryService;

    public List<Trick> findTricks(Long sportId, Long categoryId, String difficulty) {
        Optional<Category> category = categoryService.findCategory(sportId, categoryId);

        if(category.isPresent()) {
            return Objects.equals(difficulty, "all") ?
                    trickRepository.findAll(categoryId) :
                    trickRepository.findAll(categoryId, difficulty);
        }

        return List.of();
    }

    public Optional<Trick> findTrickById(Long sportId, Long categoryId, Long trickId) {
        Optional<Category> category = categoryService.findCategory(sportId, categoryId);
        if(category.isPresent()) {
            return trickRepository.findById(categoryId, trickId);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Trick> createTrick(Long sportId, Long categoryId, Trick trick) {
        Optional<Category> category = categoryService.findCategory(sportId, categoryId);

        if (category.isPresent()) {
            trick.setCategory(category.get());
            return Optional.of(trickRepository.save(trick));
        }
        return Optional.empty();
    }

    public Optional<Trick> updateTrick(Long sportId, Long categoryId, Trick newTrick, Long trickId) {
        Optional<Category> category = categoryService.findCategory(sportId, categoryId);
        Optional<Trick> existingTrick = trickRepository.findById(categoryId, trickId);
        if(category.isPresent() && existingTrick.isPresent()) {
            newTrick.setId(trickId);
            newTrick.setCategory(category.get());
            return Optional.of(trickRepository.save(newTrick));
        }
        return Optional.empty();
    }

    public Optional<Trick> removeTrick(Long sportId, Long categoryId, Long trickId) {
        Optional<Category> category = categoryService.findCategory(sportId, categoryId);
        Optional<Trick> deletedTrick = trickRepository.findById(categoryId, trickId);
        if(deletedTrick.isPresent() && category.isPresent()) {
            trickRepository.delete(deletedTrick.get());
            return deletedTrick;
        }
        return Optional.empty();
    }
}
