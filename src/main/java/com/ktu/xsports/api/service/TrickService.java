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
    private final CategoryService categoryService;
    private final UserService userService;

    public List<TrickVariant> findTricks(Long sportId, Long categoryId, String difficulty, Long userId) {
        categoryService.findCategory(sportId, categoryId);
        List<TrickVariant> trickVariants = trickVariantRepository.findAll("Standard");
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
//        setTrickVariantChildren(trickVariant, userId);
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

    @Transactional
    public TrickVariant createTrick(Long sportId, Long categoryId, Trick trick) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);

        Optional<Trick> existing = trickRepository.findByName(trick.getName());
        if(existing.isPresent()) {
            throw new ServiceException("Trick already exists");
        }

        Trick newTrick = trickRepository.save(trick);

        trick.getTrickVariants()
            .forEach(variant -> {
                variant.setTrick(newTrick);
                variant.setVideoUrl("nonde");
                trickVariantRepository.save(variant);
            });

        return trickVariantRepository.findMainVariant(newTrick.getId())
            .orElseThrow(() -> new ServiceException("Something went wrong, while searching for variant."));
    }

    @Transactional
    public TrickVariant updateTrick(Long sportId, Long categoryId, Trick trick, Long trickId) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);

        trickRepository.findById(trickId)
            .orElseThrow(() -> new ServiceException("Trick not found"));
        trick.setId(trickId);

        Optional<Trick> existing = trickRepository.findByNameAndIdIsNot(trick.getName(), trickId);
        if(existing.isPresent()) {
            throw new ServiceException("Trick already exists");
        }

        Trick newTrick = trickRepository.save(trick);

        trick.getTrickVariants()
            .forEach(variant -> {
                variant.setTrick(newTrick);
                variant.setVideoUrl("nonde");
                trickVariantRepository.save(variant);
            });

        return trickVariantRepository.findMainVariant(newTrick.getId())
            .orElseThrow(() -> new ServiceException("Something went wrong, while searching for variant."));
    }

    public Optional<Trick> removeTrick(Long sportId, Long categoryId, Long trickId) {
        categoryService.findCategory(sportId, categoryId);
        Optional<Trick> deletedTrick = trickRepository.findById(categoryId, trickId);
        if(deletedTrick.isPresent()) {
            trickRepository.delete(deletedTrick.get());
            return deletedTrick;
        }
        return Optional.empty();
    }
}
