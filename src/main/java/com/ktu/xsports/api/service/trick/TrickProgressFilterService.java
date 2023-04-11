package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.TrickVariant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrickProgressFilterService {
    public void filterProgressByUser(List<TrickVariant> trickVariants, long userId) {
        trickVariants.forEach(trickVariant -> {
            filterProgressByUser(trickVariant, userId);
        });
    }

    public void filterProgressByUser(TrickVariant trickVariant, long userId) {
        setProgress(trickVariant, userId);
        setTrickVariantParentsProgress(trickVariant, userId);
        setTrickVariantChildrenProgress(trickVariant, userId);
        setTrickVariantVariantsProgress(trickVariant, userId);
    }

    private void setProgress(TrickVariant trickVariant, Long userId) {
        trickVariant.setProgress(
            trickVariant.getProgress()
                .stream()
                .filter(progress -> progress.getUser().getId() == userId)
                .toList()
        );
    }

    private void setTrickVariantParentsProgress(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick()
            .getTrickParents()
            .forEach(parent ->
                parent.getTrickVariants()
                    .forEach(
                        variant -> setProgress(variant, userId)
                    )
            );
    }

    private void setTrickVariantChildrenProgress(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick()
            .getTrickChildren()
            .forEach(parent ->
                parent.getTrickVariants()
                    .forEach(
                        variant -> setProgress(variant, userId)
                    )
            );
    }

    private void setTrickVariantVariantsProgress(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick()
            .getTrickVariants()
            .forEach(variant -> setProgress(variant, userId));
    }
}
