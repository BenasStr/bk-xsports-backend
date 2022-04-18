package com.ktu.xsports.api.service.category.internal;

import com.ktu.xsports.api.domain.Category;

import java.util.Optional;

public interface CategoryRemover {
    Optional<Category> removeCategory(long id);
}
