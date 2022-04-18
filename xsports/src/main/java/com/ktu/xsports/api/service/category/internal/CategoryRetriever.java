package com.ktu.xsports.api.service.category.internal;

import com.ktu.xsports.api.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRetriever {
    List<Category> findCategories(long sportId);

    Optional<Category> findCategoryById(long categoryIds);
}
