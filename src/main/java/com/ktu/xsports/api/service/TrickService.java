package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrickService {

    private final TrickRepository trickRepository;
    private final TrickVariantRepository trickVariantRepository;
    private final VariantService variantService;
    private final CategoryService categoryService;

    public List<TrickVariant> findTricks(long sportId, long categoryId, long userId, String variant, String search) {
        categoryService.findCategory(sportId, categoryId);
        List<TrickVariant> trickVariants = trickVariantRepository.findByNameContaining(variant, sportId, categoryId, search);
        trickVariants.forEach(trickVariant -> {
            setProgress(trickVariant, userId);
            setTrickVariantParents(trickVariant, userId);
            setTrickVariantChildren(trickVariant, userId);
            setTrickVariantVariants(trickVariant, userId);
        });
        return trickVariants;
    }

    public List<TrickVariant> findTricks(Long sportId, Long categoryId, String difficulty, Long userId, String variant) {
        categoryService.findCategory(sportId, categoryId);
        List<TrickVariant> trickVariants = trickVariantRepository.findAll(variant, sportId, categoryId);
        trickVariants.forEach(trickVariant -> {
            setProgress(trickVariant, userId);
            setTrickVariantParents(trickVariant, userId);
            setTrickVariantChildren(trickVariant, userId);
            setTrickVariantVariants(trickVariant, userId);
        });
        return trickVariants;
    }

    public TrickVariant findTrickVariantById(Long sportId, Long categoryId, Long trickId, Long userId) {
        TrickVariant trickVariant = findTrickVariantById(sportId, categoryId, trickId);

        setTrickVariantParents(trickVariant, userId);
        setTrickVariantChildren(trickVariant, userId);
        setTrickVariantVariants(trickVariant, userId);
        setProgress(trickVariant, userId);

        return trickVariant;
    }

    public TrickVariant findTrickVariantById(Long sportId, Long categoryId, Long trickId) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantRepository.findById(trickId, categoryId, sportId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));
    }

    private void setTrickVariantParents(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick()
            .getTrickParents()
            .forEach(parent ->
                parent.getTrickVariants()
                    .forEach(
                        variant -> setProgress(variant, userId)
                    )
            );
    }

    private void setTrickVariantChildren(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick()
            .getTrickChildren()
            .forEach(parent ->
                parent.getTrickVariants()
                    .forEach(
                        variant -> setProgress(variant, userId)
                    )
            );
    }

    private void setTrickVariantVariants(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick()
            .getTrickVariants()
            .forEach(variant -> setProgress(variant, userId));
    }

    private void setTrickVariantVariants(TrickVariant trickVariant) {
        trickVariant.getTrick().setTrickVariants(
            trickVariant.getTrick().getTrickVariants()
                .stream()
                .filter(variant ->
                    variant.getId() != trickVariant.getId())
                .toList()
        );
    }

    private void setProgress(TrickVariant trickVariant, Long userId) {
        trickVariant.setProgress(
            trickVariant.getProgress()
                .stream()
                .filter(progress -> progress.getUser().getId() == userId)
                .toList()
        );
    }

    public Trick findTrickVariantById(Long trickId) {
        return trickRepository.findById(trickId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist!"));
    }

    public TrickVariant createTrick(Long sportId, Long categoryId, Trick trick) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);

        Optional<Trick> existing = trickRepository.findByName(trick.getName());
        if(existing.isPresent()) {
            throw new ServiceException("Trick already exists");
        }

        Trick savedTrick = trickRepository.save(trick);
        trick.getTrickVariants()
            .forEach(variant -> {
                variant.setTrick(savedTrick);
                variant.setVariant(variantService.getMainVariant());
                variant.setVideoUrl("none");
                trickVariantRepository.save(variant);
            });

        TrickVariant standardVariant = trickVariantRepository.findMainVariant(savedTrick.getId())
            .orElseThrow(() -> new ServiceException("Something went wrong!"));
        setTrickVariantVariants(standardVariant);

        return standardVariant;
    }

    public TrickVariant createTrickVariant(Long sportId, Long categoryId, Long trickVariantId, TrickVariant trickVariant) {
        categoryService.findCategory(sportId, categoryId);
        TrickVariant trick = findTrickVariantById(sportId, categoryId, trickVariantId);
        trickVariant.setTrick(trick.getTrick());
        trickVariant.setVideoUrl("nope");
        return trickVariantRepository.save(trickVariant);
    }

    @Transactional
    public TrickVariant updateTrick(Long sportId, Long categoryId, Trick trick, Long trickVariantId) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);

        TrickVariant trickVariant = findTrickVariantById(sportId, categoryId, trickVariantId);
        trick.setId(trickVariantId);
        trick.setTrickVariants(trickVariant.getTrick().getTrickVariants());
        trick.setTrickChildren(trickVariant.getTrick().getTrickChildren());
        trick.setTrickParents(trickVariant.getTrick().getTrickParents());

        Optional<Trick> existing = trickRepository.findByName(trick.getName());
        if(existing.isPresent() && trickVariantId != existing.get().getId()) {
            throw new ServiceException("Trick already exists");
        }

        Trick updatedTrick = trickRepository.save(trick);

        trick.getTrickVariants()
            .forEach(variant -> {
                //TODO check for trick variants;
//                variant.setId(updatedTrick);
                variant.setTrick(updatedTrick);
                variant.setVariant(variantService.getMainVariant());
                variant.setVideoUrl("nonde");
                trickVariantRepository.save(variant);
            });

        TrickVariant standardVariant = trickVariantRepository.findMainVariant(updatedTrick.getId())
            .orElseThrow(() -> new ServiceException("Something went wrong!"));
        setTrickVariantVariants(standardVariant);

        return standardVariant;
    }

    public TrickVariant updateVariant(Long sportId, Long categoryId, Long trickId, Long variantId, TrickVariant trickVariant) {
        TrickVariant trick = findTrickVariantById(sportId, categoryId, trickId);
        trickVariant.setTrick(trick.getTrick());
        TrickVariant existingVariant = trickVariantRepository.findById(variantId)
            .orElseThrow(() -> new ServiceException("Variant doesn't exist!"));
        trickVariant.setId(existingVariant.getId());

        if(trickVariant.getVideoUrl() == null) {
            trickVariant.setVideoUrl(existingVariant.getVideoUrl());
        }

        return trickVariantRepository.save(trickVariant);
    }

    public void removeTrick(Long sportId, Long categoryId, Long trickId) {
        categoryService.findCategory(sportId, categoryId);
        TrickVariant trick = findTrickVariantById(sportId, categoryId, trickId);
        trick.getTrick()
                .getTrickVariants()
                    .forEach(variant -> trickVariantRepository.deleteById(variant.getId()));
        trickRepository.deleteById(trick.getTrick().getId());
    }

    public void removeTrickVariant(Long sportId, Long categoryId, Long trickId, Long variantId) {
        categoryService.findCategory(sportId, categoryId);
        findTrickVariantById(sportId, categoryId, variantId);
        trickVariantRepository.deleteById(variantId);
    }
}
