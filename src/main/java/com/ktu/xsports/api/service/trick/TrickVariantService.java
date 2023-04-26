package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.service.media.VideoService;
import com.ktu.xsports.api.specification.TrickVariantSpecification;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import com.ktu.xsports.api.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.ktu.xsports.api.util.Prefix.TRICK_FILE;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.Role.USER;

@Service
@RequiredArgsConstructor
public class TrickVariantService {
    private final TrickVariantRepository trickVariantRepository;
    private final VariantService variantService;
    private final VideoService videoService;

    public List<TrickVariant> findTricks(long categoryId, String variant, String search, String publishStatus, String difficulty, boolean missingVideo, User user) {
        TrickVariantSpecification spec;
        if (user.getRole().equals(USER)) {
            spec = TrickVariantSpecification.builder()
                .categoryId(categoryId)
                .search(search)
                .publishStatus(PUBLISHED)
                .filterUpdated(false)
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
            .missingVideo(missingVideo)
            .build();
        return trickVariantRepository.findAll(spec);
    }

    public TrickVariant findTrickById(long trickId, long categoryId) {
        return trickVariantRepository.findById(trickId, categoryId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));
    }

    public TrickVariant findTrickVariantById(long trickId) {
        return trickVariantRepository.findById(trickId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));
    }

    public TrickVariant findStandardTrickVariantById(long trickId, long categoryId) {
        return trickVariantRepository.findStandardVariantById(trickId, categoryId)
            .orElseThrow(() -> new ServiceException("Trick doesn't exist"));
    }

    public TrickVariant createStandardTrick(TrickVariant trickVariant, Trick trick) {
        trickVariant.setTrick(trick);
        trickVariant.setVariant(variantService.getStandardVariant());
        return trickVariantRepository.save(trickVariant);
    }

    public TrickVariant createStandardTrickCopy(TrickVariant currentTrick, Trick trick) {
        return trickVariantRepository.save(TrickVariant.builder()
            .trick(trick)
            .variant(currentTrick.getVariant())
            .videoUrl(currentTrick.getVideoUrl())
            .description(currentTrick.getDescription())
            .shortDescription(currentTrick.getShortDescription())
            .build());
    }

    public TrickVariant createVariantsCopies(TrickVariant currentTrick, TrickVariant trickVariant, Trick trick) {
        AtomicLong updatedId = new AtomicLong(-1);
        currentTrick.getTrick().getTrickVariants()
            .forEach(variant -> {
                TrickVariant updated;
                if (variant.getVariant().getName().equals(trickVariant.getVariant().getName())) {
                    trickVariant.setTrick(trick);
                    trickVariant.setVideoUrl(variant.getVideoUrl());
                    updated = trickVariantRepository.save(trickVariant);
                } else {
                    updated = trickVariantRepository.save(TrickVariant.builder()
                        .trick(trick)
                        .shortDescription(variant.getShortDescription())
                        .description(variant.getDescription())
                        .videoUrl(variant.getVideoUrl())
                        .variant(variant.getVariant())
                        .build());
                }

                if (updated.getVariant().getName().equals("Standard")) {
                    updatedId.set(updated.getId());
                }
            });
        return trickVariantRepository.findById(updatedId.get())
            .orElseThrow(() -> new ServiceException("Server error!"));
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

    public TrickVariant updateStandardTrick(TrickVariant trickVariant, TrickVariant updated) {
        trickVariant.setId(updated.getId());
        trickVariant.setProgress(updated.getProgress());
        trickVariant.setVariant(variantService.getStandardVariant());
        if (trickVariant.getVideoUrl() == null) {
            trickVariant.setVideoUrl(updated.getVideoUrl());
        }
        return trickVariantRepository.save(trickVariant);
    }

    public TrickVariant updateTrick(TrickVariant currentTrick, TrickVariant trickVariant) {
        trickVariant.setTrick(currentTrick.getTrick());
        trickVariant.setId(currentTrick.getId());
        trickVariant.setVariant(currentTrick.getVariant());
        if (currentTrick.getVideoUrl() != null) {
            trickVariant.setVideoUrl(currentTrick.getVideoUrl());
        }

        return trickVariantRepository.save(trickVariant);
    }

    public TrickVariant uploadVideo(long categoryId, long trickId, MultipartFile video) {
        TrickVariant trickVariant = findTrickVariantById(trickId);
        String fileName = trickVariant.getVideoUrl() == null || trickVariant.getVideoUrl().equals("") ?
            videoService.uploadVideo(video, TRICK_FILE+trickVariant.getId()) :
            videoService.updateVideo(video, trickVariant.getVideoUrl());
        trickVariant.setVideoUrl(fileName);
        return trickVariantRepository.save(trickVariant);
    }

    public void removeTrickVariant(Long categoryId, Long trickId, Long variantId) {
        findTrickById(categoryId, variantId);
        trickVariantRepository.deleteById(variantId);
    }

    public void removeTrickVariant(TrickVariant trickVariant) {
        trickVariantRepository.deleteById(trickVariant.getId());
    }

    public void removeTrickVariants(List<TrickVariant> trickVariants) {
        trickVariants.forEach(trickVariant ->
            trickVariantRepository.deleteById(trickVariant.getId()));
    }

    public void removeVideos(Trick updated, Trick trickGroup) {
        updated.getTrickVariants().forEach(u -> {
            trickGroup.getTrickVariants().forEach(g -> {
                if (u.getVariant().getName().equals(g.getVariant().getName())) {
                    if (u.getVideoUrl() != null
                        && g.getVideoUrl() != null
                        && !u.getVideoUrl().equals(g.getVideoUrl())) {
                        videoService.deleteVideo(u.getVideoUrl());
                    }
                }
            });
        });
    }
}
