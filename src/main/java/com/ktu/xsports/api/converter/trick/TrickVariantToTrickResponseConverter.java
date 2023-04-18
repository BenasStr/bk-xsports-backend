package com.ktu.xsports.api.converter.trick;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.dto.response.trick.TrickBasicResponse;
import com.ktu.xsports.api.dto.response.trick.TrickExtendedResponse;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            mapPublishStatus((TrickVariant) context.getSource())
        ).map(source, destination.getPublishStatus());

        using(context ->
            mapLastUpdated((TrickVariant) context.getSource())
        ).map(source, destination.getLastUpdated());

        using(context ->
            mapVariantsCreated((TrickVariant) context.getSource())
        ).map(source, destination.getVariantsCreated());

        using(context ->
            mapParents((TrickVariant) context.getSource())
        ).map(source, destination.getTrickParents());

        using(context ->
            mapChildren((TrickVariant) context.getSource())
        ).map(source, destination.getTrickChildren());

        using(context ->
            mapVariants((TrickVariant) context.getSource())
        ).map(source, destination.getTrickVariants());
    }

    private String mapVariantsCreated(TrickVariant trickVariant) {
        int variantsCount = trickVariant.getTrick().getCategory().getSport().getVariants().size() + 1;
        int variantsCreated = trickVariant.getTrick().getTrickVariants().size();

        return String.format("%d/%d", variantsCreated, variantsCount);
    }

    private LocalDate mapLastUpdated(TrickVariant trickVariant) {
        return trickVariant.getTrick().getLastUpdated();
    }

    private List<TrickBasicResponse> mapParents(TrickVariant trickVariant) {
        String variant = trickVariant.getVariant().getName();
        List<Trick> trick = trickVariant.getTrick().getTrickParents();
        List<TrickBasicResponse> parents = new ArrayList<>();

        trick.forEach(parent -> {
                mapVariants(parent.getTrickVariants(), variant)
                    .ifPresent(parents::add);
            }
        );
        return parents;
    }

    private List<TrickBasicResponse> mapChildren(TrickVariant trickVariant) {
        String variant = trickVariant.getVariant().getName();
        List<Trick> trick = trickVariant.getTrick().getTrickChildren();
        List<TrickBasicResponse> children = new ArrayList<>();

        trick.forEach(child -> {
                mapVariants(child.getTrickVariants(), variant)
                    .ifPresent(children::add);
            }
        );
        return children;
    }

    private Optional<TrickBasicResponse> mapVariants(List<TrickVariant> trickVariants, String variant) {
        return trickVariants.stream()
            .filter(trickVariant -> trickVariant.getVariant().getName().equals(variant))
            .map(this::buildTrickBasicResponse)
            .findFirst();
    }

    private List<TrickBasicResponse> mapVariants(TrickVariant trickVariant) {
        return trickVariant.getTrick().getTrickVariants().stream()
            .map(this::buildTrickBasicResponse)
            .toList();
    }

    private TrickBasicResponse buildTrickBasicResponse(TrickVariant trickVariant) {
        return TrickBasicResponse.builder()
            .id(trickVariant.getId())
            .difficulty(mapDifficulty(trickVariant))
            .name(mapName(trickVariant))
            .shortDescription(trickVariant.getShortDescription())
            .status(mapProgress(trickVariant))
            .build();
    }
}
