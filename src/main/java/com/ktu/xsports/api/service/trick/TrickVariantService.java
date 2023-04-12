package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.specification.TrickVariantSpecification;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import com.ktu.xsports.api.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.Role.USER;

@Service
@RequiredArgsConstructor
public class TrickVariantService {
    private final TrickVariantRepository trickVariantRepository;
    private final VariantService variantService;

    public List<TrickVariant> findTricks(long categoryId, String variant, String search, String publishStatus, String difficulty, User user) {
        TrickVariantSpecification spec;
        if (user.getRole().equals(USER)) {
            spec = TrickVariantSpecification.builder()
                .categoryId(categoryId)
                .search(search)
                .publishStatus(PUBLISHED)
                .filterUpdated(false)
                .build();
            return trickVariantRepository.findAll(spec);
        }
        spec = TrickVariantSpecification.builder()
            .categoryId(categoryId)
            .search(search)
            .difficulty(difficulty)
            .publishStatus(publishStatus)
            .variant(variant)
            .filterUpdated(true)
            .build();
        return trickVariantRepository.findAll(spec);
    }

    public TrickVariant findTrickById(long trickId, long categoryId) {
        return trickVariantRepository.findById(trickId, categoryId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));
    }

    public TrickVariant findStandardTrickVariantById(long trickId, long categoryId) {
        return trickVariantRepository.findStandardVariantById(trickId, categoryId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));
    }

    public TrickVariant createStandardTrick(TrickVariant trickVariant) {
        trickVariant.setVariant(variantService.getStandardVariant());
        return trickVariantRepository.save(trickVariant);
    }

    public TrickVariant createStandardTrickCopy(TrickVariant currentTrick, TrickVariant trickVariant) {
        trickVariant.setVariant(variantService.getStandardVariant());
        trickVariant.setVideoUrl(currentTrick.getVideoUrl());
        TrickVariant updated = trickVariantRepository.save(trickVariant);
        currentTrick.setUpdatedBy(updated);
        return trickVariantRepository.save(currentTrick);
    }

    public TrickVariant createStandardTrickCopy(TrickVariant currentTrick, Trick trick) {
        TrickVariant updated = trickVariantRepository.save(TrickVariant.builder()
            .trick(trick)
            .variant(currentTrick.getVariant())
            .videoUrl(currentTrick.getVideoUrl())
            .description(currentTrick.getDescription())
            .shortDescription(currentTrick.getShortDescription())
            .build());

        currentTrick.setUpdatedBy(updated);
        trickVariantRepository.save(currentTrick);
        return updated;
    }

    public void createVariantsCopies(TrickVariant currentTrick, Trick trick) {
        currentTrick.getTrick().getTrickVariants()
            .forEach(variant -> {
                if (!variant.getVariant().getName().equals("Standard")) {
                    TrickVariant updated = trickVariantRepository.save(TrickVariant.builder()
                            .trick(trick)
                            .shortDescription(variant.getShortDescription())
                            .description(variant.getDescription())
                            .videoUrl(variant.getVideoUrl())
                            .variant(variant.getVariant())
                        .build());
                    variant.setUpdatedBy(updated);
                    trickVariantRepository.save(variant);
                }
            });
    }


    //TODO maybe use this for all tricks updates??????
    public void createVariantsCopies(TrickVariant currentTrick, TrickVariant trickVariant, Trick trick) {
        currentTrick.getTrick().getTrickVariants()
            .forEach(variant -> {
                if (!variant.getVariant().getName().equals("Standard")) {
                    if (variant.getVariant().getName().equals(trickVariant.getVariant().getName())) {
                        TrickVariant updated = trickVariantRepository.save(TrickVariant.builder()
                            .trick(trick)
                            .shortDescription(trickVariant.getShortDescription())
                            .description(trickVariant.getDescription())
                            .videoUrl(variant.getVideoUrl())
                            .variant(trickVariant.getVariant())
                            .build());
                        variant.setUpdatedBy(updated);
                        trickVariantRepository.save(variant);
                        return;
                    }
                    TrickVariant updated = trickVariantRepository.save(TrickVariant.builder()
                        .trick(trick)
                        .shortDescription(variant.getShortDescription())
                        .description(variant.getDescription())
                        .videoUrl(variant.getVideoUrl())
                        .variant(variant.getVariant())
                        .build());
                    variant.setUpdatedBy(updated);
                    trickVariantRepository.save(variant);
                }
            });
    }

    public TrickVariant createTrick(long categoryId, long trickId, TrickVariant trickVariant) {
        TrickVariant standard = findStandardTrickVariantById(trickId, categoryId);
        standard.getTrick().getTrickVariants()
            .forEach(variant -> {
                if (variant.getVariant() == trickVariant.getVariant()) {
                    throw new ServiceException("Variant for trick already exists!");
                }
            });
        trickVariant.setTrick(standard.getTrick());
        return trickVariantRepository.save(trickVariant);
    }

    public TrickVariant updateStandardTrick(TrickVariant currentTrick, TrickVariant trickVariant) {
        trickVariant.setId(currentTrick.getId());
        trickVariant.setProgress(currentTrick.getProgress());
        trickVariant.setVariant(variantService.getStandardVariant());
        if (trickVariant.getVideoUrl() == null) {
            trickVariant.setVideoUrl(currentTrick.getVideoUrl());
        }
        return trickVariantRepository.save(trickVariant);
    }

    public TrickVariant updateTrick(TrickVariant currentTrick, TrickVariant trickVariant) {
        trickVariant.setTrick(currentTrick.getTrick());
        trickVariant.setId(currentTrick.getId());
        if (trickVariant.getVideoUrl() == null) {
            trickVariant.setVideoUrl(currentTrick.getVideoUrl());
        }

        return trickVariantRepository.save(trickVariant);
    }

    public void removeTrickVariant(Long categoryId, Long trickId, Long variantId) {
        findTrickById(categoryId, variantId);
        trickVariantRepository.deleteById(variantId);
    }


    private void duplicateVariants(TrickVariant currentTrick, TrickVariant updatedVariant) {
        currentTrick.getTrick().getTrickVariants()
            .forEach(variant -> {
                if (!variant.getVariant().getName().equals("Standard")) {
                    duplicateVariant(currentTrick, updatedVariant.getTrick());
                }
            });
    }

    private void duplicateVariant(TrickVariant currentVariant, Trick trick) {
        trickVariantRepository.save(TrickVariant.builder()
            .variant(currentVariant.getVariant())
            .description(currentVariant.getDescription())
            .shortDescription(currentVariant.getShortDescription())
            .trick(trick)
            .videoUrl(currentVariant.getVideoUrl())
            .progress(currentVariant.getProgress())
            .build()
        );
    }
}
