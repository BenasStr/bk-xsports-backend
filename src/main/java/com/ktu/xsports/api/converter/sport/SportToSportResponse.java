package com.ktu.xsports.api.converter.sport;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.dto.response.SportResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Component
public class SportToSportResponse extends PropertyMap<Sport, SportResponse> {

    @Override
    protected void configure() {
        using(context ->
            mapSportContentStatus((Sport) context.getSource())
        ).map(source, destination.getContentStatus());

        using(context ->
            mapCategoriesCount((Sport) context.getSource())
        ).map(source, destination.getCategoriesCount());
    }

    private String mapSportContentStatus(Sport sport) {
        if (sport.getPublishStatus().equals(PUBLISHED)) {
            return sport.getCategories().stream()
                .filter(category ->
                    category.getTricks().stream()
                        .filter(trick -> !trick.getPublishStatus().equals(PUBLISHED))
                        .findFirst()
                        .orElse(null) != null
                    || !category.getPublishStatus().equals(PUBLISHED)
                ).findFirst()
                .orElse(null) == null ? PUBLISHED : UPDATED;
        }
        return sport.getPublishStatus();
    }

    private int mapCategoriesCount(Sport sport) {
        return (int) sport.getCategories().stream()
            .filter(category -> !category.getPublishStatus().equals(UPDATED))
            .count();
    }

    private int countCategories(List<Category> categories) {
        return (int) categories.stream()
            .filter(category -> !category.getPublishStatus().equals(UPDATED))
            .count();
    }
}
