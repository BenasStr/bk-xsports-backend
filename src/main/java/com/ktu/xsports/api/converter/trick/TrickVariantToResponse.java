package com.ktu.xsports.api.converter.trick;

import com.ktu.xsports.api.domain.TrickVariant;

public interface TrickVariantToResponse {
    default String mapName(TrickVariant trick) {
        if(trick.getVariant().getName().equals("Standard")) {
            return trick.getTrick().getName();
        }
        return String.format("%s %s",
            trick.getVariant().getName(),
            trick.getTrick().getName()
        );
    }

    default String mapProgress(TrickVariant trick) {
        if(trick.getProgress() == null || trick.getProgress().isEmpty()) {
            return null;
        }
        return trick.getProgress().stream()
            .findFirst()
            .get()
            .getStatus()
            .getName();
    }

    default String mapDifficulty(TrickVariant trick) {
        return trick.getTrick().getDifficulty().getName();
    }

    default String mapPublishStatus(TrickVariant trick) {
        return trick.getTrick().getPublishStatus();
    }

    default long mapMainId(TrickVariant trickVariant) {
        return trickVariant.getTrick().getId();
    }
}
