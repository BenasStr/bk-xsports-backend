package com.ktu.xsports.api.converter.publish;

import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.dto.response.publish.PublishItemResponse;
import com.ktu.xsports.api.dto.response.publish.PublishResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublishToPublishResponse extends PropertyMap<Publish, PublishResponse> {

    @Override
    protected void configure() {
        using(context ->
            mapSport((Publish) context.getSource())
        ).map(source, destination.getSport());

        using(context ->
            mapTricks((Publish) context.getSource())
        ).map(source, destination.getTricks());
    }

    private PublishItemResponse mapSport(Publish publish) {
        Sport sport = publish.getCategory().getSport();
        return PublishItemResponse.builder()
            .id(sport.getId())
            .name(sport.getName())
            .status(sport.getPublishStatus())
            .build();
    }

    private List<PublishItemResponse> mapTricks(Publish publish) {
        //TODO create recursive search.
        return null;
    }
}
