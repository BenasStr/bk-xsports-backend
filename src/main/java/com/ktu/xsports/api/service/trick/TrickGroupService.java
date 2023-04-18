package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.service.CategoryService;
import com.ktu.xsports.api.service.VariantService;
import com.ktu.xsports.api.service.media.VideoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ktu.xsports.api.util.Prefix.TRICK_FILE;
import static com.ktu.xsports.api.util.PublishStatus.NOT_PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.SCHEDULED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Service
@RequiredArgsConstructor
public class TrickGroupService {
    private final TrickService trickService;
    private final TrickVariantService trickVariantService;
    private final TrickProgressFilterService trickProgressFilterService;
    private final CategoryService categoryService;
    private final VariantService variantService;

    public List<TrickVariant> findTricks(long sportId, long categoryId, String variant, String search, String publishStatus, String difficulty, User user) {
        categoryService.findCategory(sportId, categoryId);
        List<TrickVariant> tricks = trickVariantService.findTricks(categoryId, variant, search, publishStatus, difficulty, user);
        trickProgressFilterService.filterProgressByUser(tricks, user.getId());
        return tricks;
    }

    public TrickVariant findTrickById(long sportId, long categoryId, long trickId, long userId) {
        categoryService.findCategory(sportId, categoryId);
        TrickVariant trick = trickVariantService.findTrickById(trickId, categoryId);
        trickProgressFilterService.filterProgressByUser(trick, userId);
        return trick;
    }

    public TrickVariant findTrickById(long sportId, long categoryId, long trickId) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.findTrickById(trickId, categoryId);
    }

    public TrickVariant findStandardTrickById(long sportId, long categoryId, long trickId) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.findStandardTrickVariantById(trickId, categoryId);
    }

    @Transactional
    public TrickVariant createStandardTrick(long sportId, long categoryId, TrickVariant trickVariant) {
        Trick trick = trickService.createTrick(sportId, categoryId, trickVariant.getTrick());
        return trickVariantService.createStandardTrick(trickVariant, trick);
    }

    @Transactional
    public TrickVariant createTrick(long sportId, long categoryId, long trickId, TrickVariant trickVariant) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.createTrick(categoryId, trickId, trickVariant);
    }

    @Transactional
    public TrickVariant updateStandardTrick(long sportId, long categoryId, long trickId, TrickVariant trickVariant) {
        TrickVariant currentTrick = findStandardTrickById(sportId, categoryId, trickId);
        if (currentTrick.getTrick().getPublishStatus().equals(PUBLISHED)) {
            Trick trick = trickService.createTrickCopy(currentTrick.getTrick(), trickVariant.getTrick());
            trickVariant.setVariant(variantService.getStandardVariant());
            return trickVariantService.createVariantsCopies(currentTrick, trickVariant, trick);
        }

        Trick trick = trickService.updateTrick(currentTrick.getTrick(), trickVariant.getTrick());
        trickVariant.setTrick(trick);
        return trickVariantService.updateStandardTrick(currentTrick, trickVariant);
    }

    @Transactional
    public TrickVariant updateTrick(long sportId, long categoryId, long trickId, long variantId, TrickVariant trickVariant) {
        TrickVariant currentTrick = findStandardTrickById(sportId, categoryId, trickId);
        if (currentTrick.getTrick().getPublishStatus().equals(PUBLISHED)) {
            Trick trick = trickService.createTrickCopy(currentTrick.getTrick());
            trickVariant.setTrick(trick);
            return trickVariantService.createVariantsCopies(currentTrick, trickVariant, trick);
        }

        TrickVariant matchedVariant = currentTrick.getTrick().getTrickVariants()
            .stream()
            .filter(variant -> variant.getId() == variantId)
            .findFirst()
            .orElseThrow(() -> new ServiceException("Given variant doesn't exist in this group!"));

        return trickVariantService.updateTrick(matchedVariant, trickVariant);
    }

    public TrickVariant uploadVideo(long sportId, long categoryId, long trickId, MultipartFile video) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.uploadVideo(categoryId, trickId, video);
    }

    @Transactional
    public void removeTrick(long sportId, long categoryId, long trickId) {
        //TODO remove all variants if it's standard and the trick itself.

        //TODO remove only that variant if it's

        //TODO save video links and remove videos.

        //TODO maybe implement DELETE publish state?
        // or should it say, that it will be deleted?
    }

    @Transactional
    public void publish(List<Trick> tricks) {
        tricks.forEach(trick ->
            trick.getTrickVariants()
                .forEach(trickVariant -> {
                    if (trickVariant.getTrick().getPublishStatus().equals(UPDATED)
                        && trickVariant.getTrick().getUpdatedBy() != null
                    ) {
                        publishUpdatedTrickGroup(trickVariant);
                    } else if (trickVariant.getTrick().getPublishStatus().equals(SCHEDULED)
                        || trickVariant.getTrick().getPublishStatus().equals(NOT_PUBLISHED)) {
                        publishCreatedTrickGroup(trickVariant);
                    }
                }
            )
        );
    }

    public void publishUpdatedTrickGroup(TrickVariant trickVariant) {
        trickVariantService.removeVideos(trickVariant.getTrick().getUpdates(), trickVariant.getTrick());

        trickService.publishUpdatedTrick(trickVariant.getTrick());
    }

    public void publishCreatedTrickGroup(TrickVariant trickVariant) {
        trickService.publishCreatedTrick(trickVariant.getTrick());
    }
}
