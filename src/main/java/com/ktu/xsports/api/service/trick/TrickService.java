package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ktu.xsports.api.util.PublishStatus.NOT_PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.SCHEDULED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Service
@RequiredArgsConstructor
public class TrickService {

    private final TrickRepository trickRepository;
    private final CategoryService categoryService;

    public Trick findTrick(long id) {
        return trickRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Trick not found!"));
    }

    public Trick findTrick(long trickId, long categoryId) {
        return trickRepository.findById(categoryId, trickId)
            .orElseThrow(() -> new ServiceException("Trick not found!"));
    }

    public Trick createTrick(long sportId, long categoryId, Trick trick) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);
        trick.setPublishStatus(NOT_PUBLISHED);
        trick.setLastUpdated(LocalDate.now());

        Optional<Trick> existing = trickRepository.findByName(trick.getName());
        if(existing.isPresent()) {
            throw new ServiceException("Trick already exists");
        }
        return trickRepository.save(trick);
    }

    public Trick createTrickCopy(Trick currentTrick, Trick trick) {
        trick.setPublishStatus(UPDATED);
        trick.setLastUpdated(LocalDate.now());
        trick.setCategory(currentTrick.getCategory());
        Trick updated = trickRepository.save(trick);
        currentTrick.setUpdatedBy(trick);
        trickRepository.save(currentTrick);
        return updated;
    }

    public Trick createTrickCopy(Trick currentTrick) {
        Trick trick = Trick.builder()
            .name(currentTrick.getName())
            .difficulty(currentTrick.getDifficulty())
            .category(currentTrick.getCategory())
            .publishStatus(UPDATED)
            .lastUpdated(LocalDate.now())
            .build();
        Trick updated = trickRepository.save(trick);
        currentTrick.setUpdatedBy(trick);
        trickRepository.save(currentTrick);
        return updated;
    }

    public Trick updateTrick(Trick currentTrick, Trick trick) {
        trick.setId(currentTrick.getId());
        trick.setPublishStatus(currentTrick.getPublishStatus());
        trick.setTrickVariants(currentTrick.getTrickVariants());
        trick.setTrickChildren(currentTrick.getTrickChildren());
        trick.setCategory(currentTrick.getCategory());
        trick.setLastUpdated(LocalDate.now());
        return trickRepository.save(trick);
    }

    public void removeTrick(long sportId, long categoryId, long trickId) {

    }

    @Transactional
    public void publishUpdatedTrick(Trick trick) {
        trick.setPublishStatus(PUBLISHED);
        trick.setLastUpdated(LocalDate.now());
        trickRepository.save(trick);
    }

    public void removeUpdatedTrick(Trick trick) {
        trickRepository.delete(trick);
    }

    public void publishCreatedTrick(Trick trick) {
        trick.setPublishStatus(PUBLISHED);
        trick.setLastUpdated(LocalDate.now());
        trickRepository.save(trick);
    }
}
