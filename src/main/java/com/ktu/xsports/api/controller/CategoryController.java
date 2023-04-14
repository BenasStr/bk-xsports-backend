package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.CategoryRequest;
import com.ktu.xsports.api.dto.response.CategoryResponse;
import com.ktu.xsports.api.service.CategoryService;
import com.ktu.xsports.api.service.media.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.ktu.xsports.api.util.ApiVersionPrefix.*;
import static com.ktu.xsports.api.util.Prefix.CATEGORY_FILE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + "/sports/{sportId}/categories")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findSportsCategories(
        @PathVariable long sportId,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "") String publishStatus,
        @AuthenticationPrincipal User user
    ) {
        log.info("Categories get called.");
        List<Category> categories = categoryService.findCategories(sportId, search, publishStatus, user);
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
        Category newCategory = categoryService.createCategory(sportId, categoryRequest.toCategory());
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(newCategory, CategoryResponse.class))
        );
    }

    @PostMapping("/{categoryId}/image")
    public ResponseEntity<?> uploadCategoryImage(
        @RequestParam("file") MultipartFile image,
        @PathVariable Long categoryId,
        @PathVariable Long sportId
    ) {
        Category category = categoryService.findCategory(sportId, categoryId);
        String fileName = category.getPhotoUrl() == null || category.getPhotoUrl().equals("") ?
            imageService.uploadImage(image, CATEGORY_FILE+category.getId()) :
            imageService.updateImage(image, category.getPhotoUrl());
        category.setPhotoUrl(fileName);
        categoryService.updateCategory(sportId, category, category.getId());
        return ResponseEntity.ok(
            Map.of("data", fileName)
        );
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateSportCategory(
        @PathVariable long categoryId,
        @RequestBody @Valid CategoryRequest categoryRequest,
        @PathVariable long sportId
    ) {
        log.info("Category update called.");
        Category newCategory = categoryService.updateCategory(sportId, categoryRequest.toCategory(), categoryId);

        return ResponseEntity.ok(
                Map.of("data", modelMapper.map(newCategory, CategoryResponse.class))
        );
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
