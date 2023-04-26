package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.service.CategoryService;
import com.ktu.xsports.api.service.VariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.DELETED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;
import static com.ktu.xsports.api.util.Role.USER;

@Service
@RequiredArgsConstructor
public class TrickGroupService {
    private final TrickService trickService;
    private final TrickVariantService trickVariantService;
    private final TrickProgressFilterService trickProgressFilterService;
    private final CategoryService categoryService;
    private final VariantService variantService;

    public List<TrickVariant> findTricks(long sportId, long categoryId, String variant, String search, String publishStatus, String difficulty, boolean missingVideo, User user) {
        categoryService.findCategory(sportId, categoryId);

        List<TrickVariant> tricks = trickVariantService.findTricks(categoryId, variant, search, publishStatus, difficulty, missingVideo, user);
        trickProgressFilterService.filterProgressByUser(tricks, user.getId());

        if (user.getRole().equals(USER)) {
            return tricks;
        }
        return applyUpdatedFieldsToTrickVariants(tricks);
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

    public TrickVariant updateStandardTrick(long sportId, long categoryId, long trickId, TrickVariant trickVariant) {
        TrickVariant existingTrick = findStandardTrickById(sportId, categoryId, trickId);

        if (existingTrick.getTrick().getPublishStatus().equals(PUBLISHED)) {
            if (existingTrick.getTrick().getUpdatedBy() == null) {
                trickVariant.setVariant(variantService.getStandardVariant());
                return updatePublishedTrickGroup(trickVariant, existingTrick);
            }
            return updateUpdatedTrickGroup(trickVariant, existingTrick);
        }
        return updateTrickGroup(trickVariant,existingTrick);
    }

    @Transactional
    private TrickVariant updatePublishedTrickGroup(TrickVariant trickVariant, TrickVariant published) {
        Trick trick = trickService.updatePublishedTrick(trickVariant.getTrick(), published.getTrick());
        return trickVariantService.createVariantsCopies(published, trickVariant, trick.getUpdatedBy());
    }

    @Transactional
    private TrickVariant updateUpdatedTrickGroup(TrickVariant trickVariant, TrickVariant updated) {
        trickService.updateUpdatedTrick(trickVariant.getTrick(), updated.getTrick());
        return trickVariantService.updateStandardTrick(trickVariant, updated);
    }

    @Transactional
    private TrickVariant updateTrickGroup(TrickVariant trickVariant, TrickVariant updated) {
        trickService.updateTrick(trickVariant.getTrick(), updated.getTrick());
        return trickVariantService.updateStandardTrick(trickVariant, updated);
    }

    public TrickVariant updateTrick(long sportId, long categoryId, long trickId, long variantId, TrickVariant trickVariant) {
        TrickVariant currentTrick = findStandardTrickById(sportId, categoryId, trickId);
        TrickVariant updatedVariant = findTrickByVariantId(variantId, currentTrick);

        if (currentTrick.getTrick().getPublishStatus().equals(PUBLISHED)) {
            if (currentTrick.getTrick().getUpdatedBy() == null) {
                return updatePublishedTrickVariant(trickVariant, updatedVariant);
            }
        }
        return updateTrickVariant(trickVariant, updatedVariant);
    }

    private TrickVariant updatePublishedTrickVariant(TrickVariant trickVariant, TrickVariant updatedVariant) {
        Trick trick = trickService.createTrickCopy(updatedVariant.getTrick());
        return trickVariantService.createVariantsCopies(trickVariant, updatedVariant, trick);
    }

    private TrickVariant updateTrickVariant(TrickVariant trickVariant, TrickVariant updatedVariant) {
        return trickVariantService.updateTrick(updatedVariant, trickVariant);
    }

    private TrickVariant findTrickByVariantId(long variantId, TrickVariant currentTrick) {
        return currentTrick.getTrick().getTrickVariants()
            .stream()
            .filter(variant -> variant.getId() == variantId)
            .findFirst()
            .orElseThrow(() -> new ServiceException("Given variant doesn't exist in this group!"));
    }

    public TrickVariant uploadVideo(long sportId, long categoryId, long trickVariantId, MultipartFile video) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.uploadVideo(categoryId, trickVariantId, video);
    }

    @Transactional
    public void removeStandardTrick(long sportId, long categoryId, long trickId) {
        TrickVariant trickVariant = findStandardTrickById(sportId, categoryId, trickId);

        if (trickVariant.getTrick().getPublishStatus().equals(DELETED)) {
            return;
        }

        if (trickVariant.getTrick().getPublishStatus().equals(PUBLISHED)) {
            if (trickVariant.getTrick().getUpdatedBy() == null) {
                removePublishedTrick(trickVariant);
                return;
            }
        }
        if (trickVariant.getTrick().getPublishStatus().equals(UPDATED)) {
            removeUpdatedTrick(trickVariant);
            return;
        }

        removeTrick(trickVariant);
    }

    private void removePublishedTrick(TrickVariant trickVariant) {
        trickService.markTrickAsRemoved(trickVariant.getTrick());
    }

    private void removeUpdatedTrick(TrickVariant trickVariant) {
        trickVariantService.removeTrickVariants(trickVariant.getTrick().getTrickVariants());
        trickService.removeUpdatedTrick(trickVariant.getTrick());
    }

    private void removeTrick(TrickVariant trickVariant) {
        trickVariantService.removeTrickVariants(trickVariant.getTrick().getTrickVariants());
        trickService.removeTrick(trickVariant.getTrick());
    }

    public void removeTrickVariant(long sportId, long categoryId, long trickId, long trickVariantId) {
        TrickVariant trickVariant = findStandardTrickById(sportId, categoryId, trickId);
        TrickVariant removeVariant = findTrickByVariantId(trickVariantId, trickVariant);

        if (trickVariant.getTrick().getPublishStatus().equals(PUBLISHED)) {
            throw new ServiceException("Can't delete published variant...");
        }
        trickVariantService.removeTrickVariant(removeVariant);
    }

    public void publish(List<Trick> tricks) {
        List<Long> modifiedTricksIds = tricks.stream()
                .filter(trick -> !trick.getPublishStatus().equals(PUBLISHED))
                .map(Trick::getId)
                .toList();

        publishAll(modifiedTricksIds);
    }

    public void publishAll(List<Long> ids) {
        //TODO redo this one. It should only copy data from objects and not change the object itself.
//        ids.forEach(id -> {
//                Trick trick = trickService.findTrick(id);
//
//                if (trick.getPublishStatus().equals(UPDATED)) {
//                    trickVariantService.removeVideos(trick.getUpdates(), trick);
//                    trickVariantService.removeTrickVariants(trick.getUpdates().getTrickVariants());
//                    Trick updated = trickService.findTrick(trick.getUpdates().getId());
//                    trickService.publishUpdatedTrick(trick);
//                    trickService.removeUpdatedTrick(updated);
//                } else if (trick.getPublishStatus().equals(SCHEDULED)
//                    || trick.getPublishStatus().equals(NOT_PUBLISHED)) {
//                    trickService.publishCreatedTrick(trick);
//                }
//            }
//        );
    }

    private List<TrickVariant> applyUpdatedFieldsToTrickVariants(List<TrickVariant> trickVariants) {
        return trickVariants.stream()
            .map(trickVariant -> {
                Trick updated = trickService.applyUpdatedFieldsToTrick(trickVariant.getTrick());
                return updated.getTrickVariants().stream()
                    .filter(variant -> variant.getVariant().getName().equals("Standard"))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("Couldn't find standard trick"));
            }).toList();
    }
}
