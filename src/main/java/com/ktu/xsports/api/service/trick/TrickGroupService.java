package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrickGroupService {
    private final TrickService trickService;
    private final TrickVariantService trickVariantService;
    private final TrickProgressFilterService trickProgressFilterService;
    private final CategoryService categoryService;

    public List<TrickVariant> findTricks(long sportId, long categoryId, String variant, String search, String publishStatus, String difficulty, long userId) {
        categoryService.findCategory(sportId, categoryId);
        List<TrickVariant> tricks = trickVariantService.findTricks(categoryId, variant, search, publishStatus, difficulty);
        trickProgressFilterService.filterProgressByUser(tricks, userId);
        return tricks;
    }

    public TrickVariant findTrickById(long sportId, long categoryId, long trickId, long userId) {
        categoryService.findCategory(sportId, categoryId);
        TrickVariant trick = trickVariantService.findTrickById(categoryId, trickId);
        trickProgressFilterService.filterProgressByUser(trick, userId);
        return trick;
    }

    public TrickVariant findTrickById(long sportId, long categoryId, long trickId) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.findTrickById(categoryId, trickId);
    }

    public TrickVariant createStandardTrick(long sportId, long categoryId, TrickVariant trickVariant) {
        Trick trick = trickService.createTrick(sportId, categoryId, trickVariant.getTrick());
        trickVariant.setTrick(trick);
        return trickVariantService.createStandardTrick(trickVariant);
    }

    public TrickVariant createTrick(long sportId, long categoryId, long trickId, TrickVariant trickVariant) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.createTrick(categoryId, trickId, trickVariant);
    }

    public TrickVariant updateStandardTrick(long sportId, long categoryId, long trickId, TrickVariant trickVariant) {
        return null;
    }

    public TrickVariant updateTrick(long sportId, long categoryId, long trickId, long variantId, TrickVariant trickVariant) {
        categoryService.findCategory(sportId, categoryId);
        return trickVariantService.updateTrick(categoryId, trickId, variantId, trickVariant);
    }

    public void removeStandardTrick(long sportId, long categoryId, long trickId) {

    }

    public void removeTrick(long sportId, long categoryId, long trickId, long variantId) {

    }

    //TODO create copy and update.
}
