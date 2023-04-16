package com.ktu.xsports.api.converter.publish;

import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.dto.response.publish.PublishCategoryResponse;
import com.ktu.xsports.api.dto.response.publish.PublishResponse;
import com.ktu.xsports.api.dto.response.publish.PublishSportResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class PublishToPublishResponse extends PropertyMap<Publish, PublishResponse> {

    @Override
    protected void configure() {
        using(context ->
            mapSport((Publish) context.getSource())
        ).map(source, destination.getSport());
    }

    private PublishSportResponse mapSport(Publish publish) {
        return PublishSportResponse.builder()
            .id(publish.getCategory().getSport().getId())
            .name(publish.getCategory().getSport().getName())
            .category(PublishCategoryResponse.builder()
                .id(publish.getCategory().getId())
                .name(publish.getName())
                .build())
            .build();
    }
}
