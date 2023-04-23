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
        List<Category> categories = categoryRepository.findAll();
        return applyUpdatedFieldsToCategories(categories);
    }

    public List<Category> findCategories(long sportId, String search, String publishStatus, User user) {
        sportService.findSportById(sportId);

        CategorySpecification spec;
        if (user.getRole().equals(USER)) {
            spec = new CategorySpecification(sportId, search, PUBLISHED, true);
            return categoryRepository.findAll(spec);
        }
        spec = new CategorySpecification(sportId, search, publishStatus, false);
        List<Category> categories = categoryRepository.findAll(spec);
        return applyUpdatedFieldsToCategories(categories);
    }

    public Category findCategory(long sportId, long categoryId) {
        sportService.findSportById(sportId);

        Category category = categoryRepository.findBySportIdAndId(sportId, categoryId)
            .orElseThrow(() -> new ServiceException("Category doesn't exist"));
        return applyUpdatedFieldsToCategory(category);
    }

    public Category createCategory(long sportId, Category category) {
        Sport sport = sportService.findSportById(sportId);
        category.setSport(sport);
        category.setPublishStatus(NOT_PUBLISHED);
        category.setLastUpdated(LocalDate.now());
        return categoryRepository.save(category);
    }

    public Category updateCategory(long sportId, Category category, long categoryId) {
        sportService.findSportById(sportId);

        Category existingCategory = findCategory(sportId, categoryId);
        category.setLastUpdated(LocalDate.now());
        category.setSport(existingCategory.getSport());

        if (existingCategory.getPhotoUrl() != null) {
            category.setPhotoUrl(existingCategory.getPhotoUrl());
        }

        if (existingCategory.getPublishStatus().equals(PUBLISHED)) {
            if (existingCategory.getUpdatedBy() == null) {
                return updatePublishedCategory(category, existingCategory);
            }
            return updateUpdatedCategory(category, existingCategory);
        }

        category.setId(categoryId);
        category.setPublishStatus(existingCategory.getPublishStatus());
        return categoryRepository.save(category);
    }

    @Transactional
    private Category updatePublishedCategory(Category category, Category published) {
        category.setPublishStatus(UPDATED);
        category = categoryRepository.save(category);
        published.setUpdatedBy(category);
        return categoryRepository.save(published);
    }

    private Category updateUpdatedCategory(Category category, Category updated) {
        category.setId(updated.getUpdatedBy().getId());
        category.setPublishStatus(UPDATED);
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

    @Transactional
    public void removeCategory(long sportId, long id) {
        Category category = categoryRepository.findBySportIdAndId(sportId, id)
            .orElseThrow(() -> new ServiceException("Category does not exist!"));

        if (category.getPublishStatus().equals(PUBLISHED)) {
            if (category.getUpdatedBy() == null) {
                removePublishedCategory(category);
                return;
            }
            removeUpdatedCategory(category);
            return;
        }
        removeCategory(category);
    }

    private void removePublishedCategory(Category published) {
        if (published.getTricks().size() != 0) {
            throw new ServiceException("Can't delete category, because it contains tricks!");
        }

        if (published.getPhotoUrl() != null) {
            imageService.deleteImage(published.getPhotoUrl());
        }

        categoryRepository.deleteById(published.getId());
    }

    @Transactional
    private void removeUpdatedCategory(Category category) {
        Category updated = category.getUpdatedBy();
        category.setUpdatedBy(null);
        categoryRepository.save(category);

        if (category.getPhotoUrl() != null
            && updated.getPhotoUrl() != null
            && !updated.getPhotoUrl().equals(category.getPhotoUrl())) {
            imageService.deleteImage(category.getPhotoUrl());
        }
        categoryRepository.deleteById(updated.getId());
    }

    private void removeCategory(Category category) {
        if (category.getTricks().size() != 0) {
            throw new ServiceException("Can't delete category, because it contains tricks!");
        }
        if (category.getPhotoUrl() != null) {
            imageService.deleteImage(category.getPhotoUrl());
        }
        categoryRepository.deleteById(category.getId());
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

    private List<Category> applyUpdatedFieldsToCategories(List<Category> categories) {
        return categories.stream()
            .map(this::applyUpdatedFieldsToCategory)
            .toList();
    }

    private Category applyUpdatedFieldsToCategory(Category category) {
        if (!category.getPublishStatus().equals(PUBLISHED)
            || category.getUpdatedBy() == null) {
            return category;
        }
        Category updated = category.getUpdatedBy();
        category.setName(updated.getName());
        category.setPhotoUrl(updated.getPhotoUrl());
        category.setPublishStatus(updated.getPublishStatus());
        category.setLastUpdated(updated.getLastUpdated());
        return category;
    }
}
