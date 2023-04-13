package com.ktu.xsports.api.converter.publish;

import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.dto.response.publish.PublishItemResponse;
import com.ktu.xsports.api.dto.response.publish.PublishResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;

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
        List<Trick> tricks = getAffectedTricks(publish.getTrickVariant());
        return tricks.stream()
            .map(trick ->
                PublishItemResponse.builder()
                    .id(trick.getId())
                    .name(trick.getName())
                    .status(trick.getPublishStatus())
                    .build()
            ).toList();
    }

    private List<Trick> getAffectedTricks(TrickVariant trickVariant) {
        List<Trick> affectedTricks = new ArrayList<>();
        addToTricksList(trickVariant.getTrick(), affectedTricks);
        return affectedTricks;
    }

    private void addToTricksList(Trick trick, List<Trick> trickList) {
        if (!trick.getPublishStatus().equals(PUBLISHED)) {
            if (!trickList.contains(trick)) {
                trickList.add(trick);
            }
            trick.getTrickParents()
                .forEach(parent -> addToTricksList(parent, trickList));
        }
    }
}
