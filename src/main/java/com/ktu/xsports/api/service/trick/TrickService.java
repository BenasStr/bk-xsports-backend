package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import com.ktu.xsports.api.service.CategoryService;
import com.ktu.xsports.api.service.VariantService;
import com.ktu.xsports.api.util.PublishStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Service
@RequiredArgsConstructor
public class TrickService {

    private final TrickRepository trickRepository;
    private final CategoryService categoryService;

    public Trick createTrick(long sportId, long categoryId, Trick trick) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);

        Optional<Trick> existing = trickRepository.findByName(trick.getName());
        if(existing.isPresent()) {
            throw new ServiceException("Trick already exists");
        }
        return trickRepository.save(trick);
    }

    public Trick updateTrick(long sportId, long categoryId, long trickId, Trick trick) {
        return null;
    }

    public void removeTrick(long sportId, long categoryId, long trickId) {

    }

//    public TrickVariant updateTrick(Long sportId, Long categoryId, Trick trick, Long trickVariantId) {
//        Category category = categoryService.findCategory(sportId, categoryId);
//        trick.setCategory(category);
//
//        TrickVariant trickVariant = findTrickVariantById(sportId, categoryId, trickVariantId);
//        trick.setId(trickVariantId);
//        trick.setTrickVariants(trickVariant.getTrick().getTrickVariants());
//        trick.setTrickChildren(trickVariant.getTrick().getTrickChildren());
//        trick.setTrickParents(trickVariant.getTrick().getTrickParents());
//
//        Optional<Trick> existing = trickRepository.findByName(trick.getName());
//        if(existing.isPresent() && trickVariantId != existing.get().getId()) {
//            throw new ServiceException("Trick already exists");
//        }
//
//        Trick updatedTrick = trickRepository.save(trick);
//
//        trick.getTrickVariants()
//            .forEach(variant -> {
//                //TODO check for trick variants;
////                variant.setId(updatedTrick);
//                variant.setTrick(updatedTrick);
//                variant.setVariant(variantService.getMainVariant());
//                variant.setVideoUrl("nonde");
//                trickVariantRepository.save(variant);
//            });
//
////        TrickVariant standardVariant = trickVariantRepository.findStandardVariantById(updatedTrick.getId())
////            .orElseThrow(() -> new ServiceException("Something went wrong!"));
////        setTrickVariantVariants(standardVariant);
//
////        return standardVariant;
//        return null;
//    }
//
//    public void removeTrick(Long sportId, Long categoryId, Long trickId) {
//        categoryService.findCategory(sportId, categoryId);
//        TrickVariant trick = findTrickVariantById(sportId, categoryId, trickId);
//        trick.getTrick()
//                .getTrickVariants()
//                    .forEach(variant -> trickVariantRepository.deleteById(variant.getId()));
//        trickRepository.deleteById(trick.getTrick().getId());
//    }
//



}
