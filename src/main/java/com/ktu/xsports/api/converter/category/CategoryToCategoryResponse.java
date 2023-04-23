package com.ktu.xsports.api.converter.category;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.dto.response.CategoryResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Component
public class CategoryToCategoryResponse extends PropertyMap<Category, CategoryResponse> {

    @Override
    protected void configure() {
        using(context ->
            mapCategoryContentStatus((Category) context.getSource())
        ).map(source, destination.getContentStatus());

        using(context ->
            mapTricksCount((Category) context.getSource())
        ).map(source, destination.getTricksCount());
    }

    private String mapCategoryContentStatus(Category category) {
        if (category.getPublishStatus().equals(PUBLISHED)) {
            return category.getTricks().stream()
                .filter(trick -> !trick.getPublishStatus().equals(PUBLISHED))
                .findFirst()
                .orElse(null) == null ? PUBLISHED : UPDATED;
        }
        return category.getPublishStatus();
    }

    private int mapTricksCount(Category category) {
        return category.getPublishStatus().equals(UPDATED) ?
            countTricks(category.getUpdates().getTricks()) :
            countTricks(category.getTricks());
    }

    private int countTricks(List<Trick> tricks) {
        return (int) tricks.stream()
            .filter(trick -> !trick.getPublishStatus().equals(UPDATED))
            .mapToLong(trick -> trick.getTrickVariants().size())
            .sum();
    }
}
