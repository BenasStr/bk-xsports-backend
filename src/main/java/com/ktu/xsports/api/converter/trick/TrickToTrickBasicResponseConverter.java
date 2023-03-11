package com.ktu.xsports.api.converter.trick;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.dto.response.TrickBasicResponse;
import org.modelmapper.PropertyMap;

public class TrickToTrickBasicResponseConverter extends PropertyMap<Trick, TrickBasicResponse> implements TrickVariantToResponse {

    @Override
    protected void configure() {
        using(context ->
            mapName(((Trick) context.getSource()))
        ).map(source, destination.getName());

        using(context ->
            mapProgress((Trick) context.getSource())
        ).map(source, destination.getStatus());

        using(context ->
            mapDifficulty((Trick) context.getSource())
        ).map(source, destination.getDifficulty());

        using(context ->
            mapCategory((Trick) context.getSource())
        ).map(source, destination.getCategoryId());

        using(context ->
            mapShortDescription((Trick) context.getSource())
        ).map(source, destination.getShortDescription());
    }

    private String mapName(Trick trick) {
        return trick.getTrickVariants()
            .stream()
            .findFirst()
            .map(this::mapName)
            .orElse(null);
    }

    private String mapProgress(Trick trick) {
        return trick.getTrickVariants()
            .stream()
            .findFirst()
            .map(this::mapProgress)
            .orElse(null);
    }

    private String mapDifficulty(Trick trick) {
        return trick.getTrickVariants()
            .stream()
            .findFirst()
            .map(this::mapDifficulty)
            .orElse(null);
    }

    private long mapCategory(Trick trick) {
        return trick.getTrickVariants()
            .stream()
            .findFirst()
            .map(this::mapCategory)
            .orElse(0L);
    }

    private String mapShortDescription(Trick trick) {
        return trick.getTrickVariants()
            .stream()
            .findFirst()
            .map(TrickVariant::getShortDescription)
            .orElse("");
    }
}
