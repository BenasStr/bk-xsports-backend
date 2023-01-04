package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.dto.request.CategoryRequest;
import com.ktu.xsports.api.dto.response.CategoryResponse;
import com.ktu.xsports.api.dto.response.SportResponse;
import com.ktu.xsports.api.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/sports/{sportId}/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findSportsCategories(
            @PathVariable long sportId) {
        List<Category> categories = categoryService.findCategories(sportId);
        List<CategoryResponse> categoriesResponse = categories.stream().map(
                c -> modelMapper.map(c, CategoryResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", categoriesResponse));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> findSportCategory(
            @PathVariable long categoryId,
            @PathVariable long sportId
    ) {
        Optional<Category> category = categoryService.findCategory(sportId, categoryId);

        return ResponseEntity.of(
                category.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createSportCategory(
            @RequestBody @Valid CategoryRequest categoryRequest,
            @PathVariable long sportId
    ) {
        Category category = categoryRequest.toCategory();
        Optional<Category> newCategory = categoryService.createCategory(sportId, category);

        return ResponseEntity.of(
                newCategory.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateSportCategory(
            @PathVariable long categoryId,
            @RequestBody @Valid CategoryRequest categoryRequest,
            @PathVariable long sportId
    ) {
        Category category = categoryRequest.toCategory();
        Optional<Category> newCategory = categoryService.updateCategory(sportId, category, categoryId);

        return ResponseEntity.of(
                newCategory.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteSportCategory(
            @PathVariable long categoryId,
            @PathVariable long sportId
    ) {
        Optional<Category> deletedSport = categoryService.removeCategory(sportId, categoryId);
        return ResponseEntity.of(
                deletedSport.map(s ->
                        Map.of("data", modelMapper.map(s, SportResponse.class))));
    }
}
