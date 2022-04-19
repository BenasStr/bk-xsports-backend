package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.dto.request.CategoryRequest;
import com.ktu.xsports.api.dto.response.CategoryResponse;
import com.ktu.xsports.api.dto.response.SportResponse;
import com.ktu.xsports.api.service.category.internal.CategoryCreator;
import com.ktu.xsports.api.service.category.internal.CategoryRemover;
import com.ktu.xsports.api.service.category.internal.CategoryRetriever;
import com.ktu.xsports.api.service.category.internal.CategoryUpdater;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class CategoryController {

    private final CategoryRetriever categoryRetriever;
    private final CategoryCreator categoryCreator;
    private final CategoryUpdater categoryUpdater;
    private final CategoryRemover categoryRemover;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findSportsCategories(
            @RequestParam @NotNull long sportId
    ) {
        List<Category> categories = categoryRetriever.findCategories(sportId);
        List<CategoryResponse> categoriesResponse = categories.stream().map(
                c -> modelMapper.map(c, CategoryResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", categoriesResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSportCategory(
            @PathVariable long id
    ) {
        Optional<Category> category = categoryRetriever.findCategoryById(id);

        return ResponseEntity.of(
                category.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createSportCategory(
            @RequestBody @Valid CategoryRequest categoryRequest
    ) {
        Category category = categoryRequest.toCategory();
        Optional<Category> newCategory = categoryCreator.createCategory(category);

        return ResponseEntity.of(
                newCategory.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSportCategory(
            @PathVariable long id,
            @RequestBody @Valid CategoryRequest categoryRequest
    ) {
        Category category = categoryRequest.toCategory();
        Optional<Category> newCategory = categoryUpdater.updateCategory(category, id);

        return ResponseEntity.of(
                newCategory.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSportCategory(
            @PathVariable long id
    ) {
        Optional<Category> deletedSport = categoryRemover.removeCategory(id);
        return ResponseEntity.of(
                deletedSport.map(s ->
                        Map.of("data", modelMapper.map(s, SportResponse.class))));
    }
}
