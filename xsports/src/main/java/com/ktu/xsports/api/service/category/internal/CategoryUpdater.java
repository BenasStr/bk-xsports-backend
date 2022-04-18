package com.ktu.xsports.api.service.category.internal;

import com.ktu.xsports.api.domain.Category;

import java.util.Optional;

public interface CategoryUpdater {
    Optional<Category> updateCategory(Category category, long id);
}
