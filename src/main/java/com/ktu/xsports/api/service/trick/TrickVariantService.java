package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.specification.TrickVariantSpecification;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import com.ktu.xsports.api.service.CategoryService;
import com.ktu.xsports.api.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrickVariantService {
    private final TrickVariantRepository trickVariantRepository;
    private final VariantService variantService;
    private final CategoryService categoryService;

    public List<TrickVariant> findTricks(long categoryId, String variant, String search, String publishStatus, String difficulty) {
        TrickVariantSpecification spec = new TrickVariantSpecification(categoryId, search, difficulty, publishStatus, variant);
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

    public TrickVariant updateStandardTrick() {
        return null;
    }

    public TrickVariant updateTrick(long categoryId, long trickId, long variantId, TrickVariant trickVariant) {
        findTrickById(categoryId, trickId);
        TrickVariant existing = findTrickById(categoryId, variantId);
        trickVariant.setTrick(existing.getTrick());
        trickVariant.setId(existing.getId());

        if (trickVariant.getVideoUrl() == null) {
            trickVariant.setVideoUrl(existing.getVideoUrl());
        }

        return trickVariantRepository.save(trickVariant);
    }

    public void removeTrickVariant(Long categoryId, Long trickId, Long variantId) {
        findTrickById(categoryId, variantId);
        trickVariantRepository.deleteById(variantId);
    }


//    public TrickVariant createTrickSetCopy(long trickVariantId) {
//        TrickVariant trickVariant = findTrickVariantById();
//
//        return createTrickCopy(trickVariant);
//    }

    private Trick createTrickCopy(TrickVariant trickVariant) {
        Trick trick = trickVariant.getTrick();
        return Trick.builder()
            .name(trick.getName())
            .difficulty(trick.getDifficulty())
            .category(trick.getCategory())
            .trickChildren(trick.getTrickChildren())
            .trickParents(trick.getTrickParents())
            .trickVariants(createTrickVariantsCopies(trick))
            .build();
    }

    private List<TrickVariant> createTrickVariantsCopies(Trick trick) {
        return trick.getTrickVariants().stream()
            .map(variant ->
                TrickVariant.builder()
                    .shortDescription(variant.getShortDescription())
                    .description(variant.getDescription())
                    .variant(variant.getVariant())
                    .videoUrl(variant.getVideoUrl())
                    .progress(variant.getProgress())
                    .trick(variant.getTrick())
                    .build()
            ).toList();
    }

}
