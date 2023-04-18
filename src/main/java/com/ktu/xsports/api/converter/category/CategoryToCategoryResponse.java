package com.ktu.xsports.api.converter.category;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.dto.response.CategoryResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Component
public class CategoryToCategoryResponse extends PropertyMap<Category, CategoryResponse> {

    @Override
    protected void configure() {
        using(context ->
            mapCategoryProgress((Category) context.getSource())
        ).map(source, destination.getPublishStatus());

        using(context ->
            mapTricksCount((Category) context.getSource())
        ).map(source, destination.getTricksCount());
    }

    private String mapCategoryProgress(Category category) {
        if (category.getPublishStatus().equals(PUBLISHED)) {
            return category.getTricks().stream()
                .filter(trick -> !trick.getPublishStatus().equals(PUBLISHED))
                .findFirst()
                .orElse(null) == null ? PUBLISHED : UPDATED;
        }
        return category.getPublishStatus();
    }

    private int mapTricksCount(Category category) {
        return (int) category.getTricks().stream()
            .mapToLong(trick -> trick.getTrickVariants().size())
            .sum();
    }
}
