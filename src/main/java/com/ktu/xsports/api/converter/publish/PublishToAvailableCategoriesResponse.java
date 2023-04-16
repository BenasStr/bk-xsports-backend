package com.ktu.xsports.api.converter.publish;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.dto.response.publish.PublishAvailableCategoriesResponse;
import com.ktu.xsports.api.dto.response.publish.PublishCategoryResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublishToAvailableCategoriesResponse extends PropertyMap<Sport, PublishAvailableCategoriesResponse> {

    @Override
    protected void configure() {
        using(context ->
            mapId((Sport) context.getSource())
        ).map(source, destination.getId());

        using(context ->
            mapName((Sport) context.getSource())
        ).map(source, destination.getName());

        using(context ->
            mapCategories((Sport) context.getSource())
        ).map(source, destination.getCategory());
    }

    private long mapId(Sport sport) {
        return sport.getId();
    }

    private String mapName(Sport sport) {
        return sport.getName();
    }

    private List<PublishCategoryResponse> mapCategories(Sport sports) {
        return sports.getCategories().stream()
            .map(category ->
                PublishCategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build()
            ).toList();
    }


}
