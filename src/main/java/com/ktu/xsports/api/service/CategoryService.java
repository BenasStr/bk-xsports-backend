package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.CategoryRepository;
import com.ktu.xsports.api.service.media.ImageService;
import com.ktu.xsports.api.specification.CategorySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ktu.xsports.api.util.Prefix.CATEGORY_FILE;
import static com.ktu.xsports.api.util.PublishStatus.DELETED;
import static com.ktu.xsports.api.util.PublishStatus.NOT_PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.SCHEDULED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;
import static com.ktu.xsports.api.util.Role.USER;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;
    private final SportService sportService;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findCategories(long sportId, String search, String publishStatus, User user) {
        Sport sport = sportService.findSportById(sportId);
//        sportId = sport.getUpdates() == null ?
//            sport.getId() :
//            sport.getUpdates().getId();

        CategorySpecification spec;
        if (user.getRole().equals(USER)) {
            spec = new CategorySpecification(sportId, search, PUBLISHED, true);
        } else {
            spec = new CategorySpecification(sportId, search, publishStatus, false);
        }
        return categoryRepository.findAll(spec);
    }

    public Category findCategory(long sportId, long categoryId) {
        Sport sport = sportService.findSportById(sportId);
//        sportId = sport.getUpdates() == null ? sport.getId() : sport.getUpdates().getId();

        return categoryRepository.findBySportIdAndId(sportId, categoryId)
            .orElseThrow(() -> new ServiceException("Category doesn't exist"));
    }

    public Category createCategory(long sportId, Category category) {
        Sport sport = sportService.findSportById(sportId);
        category.setSport(sport);
        category.setPublishStatus(NOT_PUBLISHED);
        category.setLastUpdated(LocalDate.now());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(long sportId, Category category, long categoryId) {
        sportService.findSportById(sportId);

        Category existingCategory = findCategory(sportId, categoryId);
        category.setLastUpdated(LocalDate.now());
        category.setSport(existingCategory.getSport());
        category.setUpdates(existingCategory.getUpdates());
        if (existingCategory.getPhotoUrl() != null) {
            category.setPhotoUrl(existingCategory.getPhotoUrl());
        }

        if (existingCategory.getPublishStatus().equals(PUBLISHED)) {
            category.setPublishStatus(UPDATED);
            Category updated = categoryRepository.save(category);
            existingCategory.setUpdatedBy(updated);
            categoryRepository.save(existingCategory);
            return categoryRepository.findById(updated.getId())
                .orElseThrow(() -> new ServiceException("Failed updating category!"));
        }

        category.setId(categoryId);
        category.setPublishStatus(existingCategory.getPublishStatus());
        return categoryRepository.save(category);
    }

    public Category updateCategoryImage(long sportId, long categoryId, MultipartFile image) {
        Category category = findCategory(sportId, categoryId);
        String fileName = category.getPhotoUrl() == null || category.getPhotoUrl().equals("") ?
            imageService.uploadImage(image, CATEGORY_FILE+category.getId()) :
            imageService.updateImage(image, category.getPhotoUrl());
        category.setPhotoUrl(fileName);
        return categoryRepository.save(category);
    }


    //TODO look at this one tomorrow
    @Transactional
    public void removeCategory(long sportId, long id) {
        Category category = findCategory(sportId, id);

        if (category.getPublishStatus().equals(UPDATED)) {
            Optional<Category> updated = categoryRepository.findUpdatedBy(category.getId());
            if(updated.isPresent()) {
                updated.get().setUpdatedBy(null);
                categoryRepository.save(updated.get());
                if(category.getPhotoUrl() != null &&
                    updated.get().getUpdatedBy() != null &&
                    !category.getPhotoUrl().equals(updated.get().getPhotoUrl())) {
                    imageService.deleteImage(category.getPhotoUrl());
                }
            }
        } else if (category.getUpdatedBy() != null) {
            categoryRepository.findById(category.getId())
                .ifPresent(updatedBy -> {
                    updatedBy.setPublishStatus(PUBLISHED);
                    categoryRepository.save(updatedBy);
                });
        }

        if (category.getPhotoUrl() != null) {
            imageService.deleteImage(category.getPhotoUrl());
        }

        categoryRepository.delete(category);
    }

    public void publish(Category category) {
        if (category.getPublishStatus().equals(PUBLISHED)
            && category.getUpdatedBy() != null) {
            publishUpdatedCategory(category);
        } else if (category.getPublishStatus().equals(NOT_PUBLISHED)
            || category.getPublishStatus().equals(SCHEDULED)
        ) {
            publishCreatedCategory(category);
        } else if (category.getPublishStatus().equals(DELETED)) {
            categoryRepository.deleteById(category.getId());
        }
    }

    @Transactional
    private void publishUpdatedCategory(Category category) {
        Category updatedBy = category.getUpdatedBy();

        String imageName = category.getPhotoUrl();
        category.setPhotoUrl(updatedBy.getPhotoUrl());
        category.setName(updatedBy.getName());
        category.setLastUpdated(LocalDate.now());
        category.setPublishStatus(PUBLISHED);
        category.setUpdatedBy(null);

        categoryRepository.save(category);

        if (imageName != null
            && !imageName.equals(updatedBy.getPhotoUrl())) {
            imageService.deleteImage(imageName);
        }
        categoryRepository.delete(updatedBy);
    }

    private void publishCreatedCategory(Category category) {
        category.setLastUpdated(LocalDate.now());
        category.setPublishStatus(PUBLISHED);
        categoryRepository.save(category);
    }
}
