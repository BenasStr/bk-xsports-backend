package com.ktu.xsports.api.converter.trick;

import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.dto.response.trick.TrickExtendedResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class TrickVariantToTrickResponseConverter extends PropertyMap<TrickVariant, TrickExtendedResponse> implements TrickVariantToResponse {

    @Override
    protected void configure() {
        using(context ->
            mapName((TrickVariant) context.getSource())
        ).map(source, destination.getName());

        using(context ->
            mapProgress((TrickVariant) context.getSource())
        ).map(source, destination.getStatus());

        using(context ->
            mapDifficulty((TrickVariant) context.getSource())
        ).map(source, destination.getDifficulty());

        using(context ->
            mapCategory((TrickVariant) context.getSource())
        ).map(source, destination.getCategoryId());
    }
}
