package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.ProgressRepository;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;

@Service
@RequiredArgsConstructor
public class TrickService {

    private final TrickRepository trickRepository;
    private final TrickVariantRepository trickVariantRepository;

    private final VariantService variantService;
    private final CategoryService categoryService;
    private final UserService userService;

    public List<TrickVariant> findTricks(Long sportId, Long categoryId, String difficulty, Long userId, String variant) {
        categoryService.findCategory(sportId, categoryId);
        List<TrickVariant> trickVariants = trickVariantRepository.findAll(variant);
        trickVariants.forEach(trickVariant -> setProgress(trickVariant, userId));

        return trickVariants;
    }

    public List<Trick> findTricks(Long sportId, Long categoryId, String difficulty, String email) {
        User user = userService.findByEmail(email);
        Category category = categoryService.findCategory(sportId, categoryId);

        return difficulty.equals("all") ?
            trickRepository.findAll(user.getId(), categoryId) :
            trickRepository.findAll(user.getId(), categoryId, difficulty);
    }

    public Optional<Trick> findTrickById(Long sportId, Long categoryId, Long trickId) {
        categoryService.findCategory(sportId, categoryId);
        return trickRepository.findById(categoryId, trickId);
    }

    public TrickVariant findTrickById(Long sportId, Long categoryId, Long trickId, Long userId) {
        categoryService.findCategory(sportId, categoryId);
        TrickVariant trickVariant = trickVariantRepository.findById(categoryId, trickId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));

        setTrickVariantParents(trickVariant, userId);
        setTrickVariantChildren(trickVariant, userId);
        setTrickVariantVariants(trickVariant, userId);
        setProgress(trickVariant, userId);

        return trickVariant;
    }

    private void setTrickVariantParents(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick().getTrickParents()
            .forEach(parent ->
                parent.setTrickVariants(
                    parent.getTrickVariants()
                        .stream()
                        .filter(variant -> variant.getVariant().getId() == trickVariant.getVariant().getId())
                        .peek(variant -> setProgress(variant, userId))
                        .toList()
                )
            );
    }

    private void setTrickVariantChildren(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick().getTrickChildren()
            .forEach(parent ->
                parent.setTrickVariants(
                    parent.getTrickVariants()
                        .stream()
                        .filter(variant -> variant.getVariant().getId() == trickVariant.getVariant().getId())
                        .peek(variant -> setProgress(variant, userId))
                        .toList()
                )
            );
    }

    private void setTrickVariantVariants(TrickVariant trickVariant, Long userId) {
        trickVariant.getTrick().setTrickVariants(
            trickVariant.getTrick().getTrickVariants()
                .stream()
                .filter(variant ->
                    variant.getId() != trickVariant.getId())
                .peek(variant ->
                    setProgress(variant, userId))
                .toList()
        );
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

    public Trick findTrickById(Long trickId) {
        return trickRepository.findById(trickId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));
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

    public TrickVariant createTrickVariant(Long sportId, Long categoryId, Long trickId, TrickVariant trickVariant) {
        categoryService.findCategory(sportId, categoryId);
        Trick trick = findTrickById(trickId);
        trickVariant.setTrick(trick);
        trickVariant.setVideoUrl("nope");
        return trickVariantRepository.save(trickVariant);
    }

    @Transactional
    public TrickVariant updateTrick(Long sportId, Long categoryId, Trick trick, Long trickId) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);

        trickRepository.findById(trickId)
            .orElseThrow(() -> new ServiceException("Trick not found"));
        trick.setId(trickId);

        Optional<Trick> existing = trickRepository.findByName(trick.getName());
        if(existing.isPresent() && trickId != existing.get().getId()) {
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
        categoryService.findCategory(sportId, categoryId);
        Trick trick = findTrickById(trickId);
        trickVariant.setTrick(trick);
        TrickVariant existingVariant = trickVariantRepository.findById(variantId)
            .orElseThrow(() -> new ServiceException("Variant doesn't exist!"));
        trickVariant.setVideoUrl("nope");
        return trickVariantRepository.save(trickVariant);
    }

    public void removeTrick(Long sportId, Long categoryId, Long trickId) {
        categoryService.findCategory(sportId, categoryId);
        Trick trick = findTrickById(trickId);
        trick.getTrickVariants()
            .forEach(variant -> trickVariantRepository.deleteById(variant.getId()));
        trickRepository.deleteById(trickId);
    }

    public void removeTrickVariant(Long sportId, Long categoryId, Long trickId, Long variantId) {
        categoryService.findCategory(sportId, categoryId);
        findTrickById(trickId);
        trickVariantRepository.deleteById(variantId);
    }
}
