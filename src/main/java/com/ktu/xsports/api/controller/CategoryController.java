package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.dto.request.CategoryRequest;
import com.ktu.xsports.api.dto.response.CategoryResponse;
import com.ktu.xsports.api.dto.response.SportResponse;
import com.ktu.xsports.api.service.CategoryService;
import com.ktu.xsports.api.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ktu.xsports.api.util.Prefix.CATEGORY_FILE;
import static com.ktu.xsports.api.util.Prefix.SPORT_FILE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/sports/{sportId}/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findSportsCategories(
            @PathVariable long sportId) {
        log.info("Categories get called.");
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
        log.info("Category get called.");
        Category category = categoryService.findCategory(sportId, categoryId);

        return ResponseEntity.ok(
                Map.of("data", modelMapper.map(category, CategoryResponse.class))
        );
    }

    @PostMapping()
    public ResponseEntity<?> createSportCategory(
            @RequestBody @Valid CategoryRequest categoryRequest,
            @PathVariable Long sportId
    ) {
        log.info("Category create called.");
        Category category = categoryRequest.toCategory();
        Optional<Category> newCategory = categoryService.createCategory(sportId, category);

        return ResponseEntity.of(
                newCategory.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @PostMapping("/{categoryId}/image")
    public ResponseEntity<?> uploadCategoryImage(
        @RequestParam("file") MultipartFile image,
        @PathVariable int categoryId,
        @PathVariable long sportId
    ) {
        Category category = categoryService.findCategory(sportId, categoryId);
        String fileName = imageService.uploadImage(image, CATEGORY_FILE+category.getId());
        category.setPhotoUrl(fileName);
        categoryService.updateCategory(sportId, category, category.getId());
        return ResponseEntity.ok("");
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateSportCategory(
            @PathVariable long categoryId,
            @RequestBody @Valid CategoryRequest categoryRequest,
            @PathVariable long sportId
    ) {
        log.info("Category update called.");
        Category category = categoryRequest.toCategory();
        Optional<Category> newCategory = categoryService.updateCategory(sportId, category, categoryId);

        return ResponseEntity.of(
                newCategory.map(c -> Map.of("data", modelMapper.map(c, CategoryResponse.class))));
    }

    @PutMapping("/category/{categoryId}/image")
    public ResponseEntity<?> updateUserProfileImage(
        @RequestParam("file") MultipartFile file,
        @PathVariable long sportId,
        @PathVariable long categoryId
    ) {
        log.info("User is updating profile picture");
        Category category = categoryService.findCategory(sportId, categoryId);
        String fileName = category.getPhotoUrl() == null ?
            imageService.uploadImage(file, CATEGORY_FILE+category.getId()) :
            imageService.updateImage(file, category.getPhotoUrl());

        return ResponseEntity.ok(Map.of("data", fileName));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteSportCategory(
        @PathVariable Long categoryId,
        @PathVariable Long sportId
    ) {
        log.info("Category delete called.");
        categoryService.removeCategory(sportId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}/image")
    public ResponseEntity<?> deleteCategoryImage(
        @PathVariable long categoryId,
        @PathVariable long sportId
    ) {
        log.info("Category image delete");
        Category category = categoryService.findCategory(sportId, categoryId);
        imageService.deleteImage(category.getPhotoUrl());
        category.setPhotoUrl(null);
        return ResponseEntity.ok("");
    }
}
