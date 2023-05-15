package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.service.CategoryService;
import com.ktu.xsports.api.specification.TrickSpecification;
import com.ktu.xsports.api.specification.TrickVariantSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ktu.xsports.api.util.PublishStatus.DELETED;
import static com.ktu.xsports.api.util.PublishStatus.NOT_PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;
import static com.ktu.xsports.api.util.Role.USER;

@Service
@RequiredArgsConstructor
public class TrickService {

    private final TrickRepository trickRepository;
    private final CategoryService categoryService;

    public Trick findTrick(long id) {
        return trickRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Trick not found!"));
    }

    public List<Trick> findTricks(long categoryId, String search, String publishStatus, String difficulty, Boolean missingVideo, Boolean missingVariants, User user) {
        TrickSpecification spec;
        if (user.getRole().equals(USER)) {
            spec = TrickSpecification.builder()
                .categoryId(categoryId)
                .search(search)
                .publishStatus(PUBLISHED)
                .filterUpdated(false)
                .build();
            return trickRepository.findAll(spec);
        }
        spec = TrickSpecification.builder()
            .categoryId(categoryId)
            .search(search)
            .difficulty(difficulty)
            .publishStatus(publishStatus)
            .filterUpdated(true)
            .missingVideo(missingVideo)
            .missingVariants(missingVariants)
            .build();
        return trickRepository.findAll(spec);
    }

    public Trick createTrick(long sportId, long categoryId, Trick trick) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);
        trick.setPublishStatus(NOT_PUBLISHED);
        trick.setLastUpdated(LocalDate.now());

        Optional<Trick> existing = trickRepository.findByName(trick.getName());
        if(existing.isPresent()) {
            if (existing.get().getCategory().getId() == categoryId) {
                throw new ServiceException("Trick already exists");
            }
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

    @Transactional
    public Trick createTrickCopy(Trick currentTrick) {
        Trick trick = Trick.builder()
            .name(currentTrick.getName())
            .difficulty(currentTrick.getDifficulty())
            .category(currentTrick.getCategory())
            .publishStatus(UPDATED)
            .lastUpdated(LocalDate.now())
            .trickParents(List.of())
            .trickChildren(List.of())
            .trickVariants(List.of())
            .build();
        Trick updated = trickRepository.save(trick);
        currentTrick.setUpdatedBy(trick);
        trickRepository.save(currentTrick);
        return updated;
    }

    @Transactional
    public Trick updatePublishedTrick(Trick trick, Trick published) {
        trick.setPublishStatus(UPDATED);
        trick.setLastUpdated(LocalDate.now());
        trick.setCategory(published.getCategory());
        trick = trickRepository.save(trick);
        published.setUpdatedBy(trick);
        return trickRepository.save(published);
    }

    public Trick updateUpdatedTrick(Trick trick, Trick updated) {
        trick.setId(updated.getUpdatedBy().getId());
        trick.setPublishStatus(UPDATED);
        trick.setLastUpdated(LocalDate.now());
        //TODO trick parents???
        trick.setTrickChildren(updated.getTrickChildren());
        trick.setTrickVariants(updated.getTrickVariants());
        trick.setCategory(updated.getCategory());
        return trickRepository.save(trick);
    }

    //TODO update
    public Trick updateTrick(Trick trick, Trick updated) {
        trick.setId(updated.getId());
        trick.setLastUpdated(LocalDate.now());
        trick.setPublishStatus(updated.getPublishStatus());
        //TODO trick parents???
        trick.setTrickChildren(updated.getTrickChildren());
        trick.setTrickVariants(updated.getTrickVariants());
        trick.setCategory(updated.getCategory());
        return trickRepository.save(trick);
    }

    public void removeTrick(Trick trick) {
        trickRepository.deleteById(trick.getId());
    }

    @Transactional
    public void removeUpdatedTrick(Trick trick, Trick updated) {
        updated.setUpdatedBy(null);
        trickRepository.save(updated);
        trickRepository.deleteById(trick.getId());
    }

    public void markTrickAsRemoved(Trick trick) {
        int notDeletedCount = (int) trick.getTrickChildren().stream()
            .filter(t -> !t.getPublishStatus().equals(DELETED))
            .count();

        if (notDeletedCount > 0) {
            throw new ServiceException("Can't delete trick, because it has children");
        }
        trick.setPublishStatus(DELETED);
        trickRepository.save(trick);
    }

    @Transactional
    public void publishUpdatedTrick(Trick trick) {
        Trick update = trick.getUpdatedBy();

        trick.setUpdatedBy(null);
        trick.setTrickParents(update.getTrickParents()); //This will present issues... As parent id is done differently
        trick.setName(update.getName());
        trick.setDifficulty(update.getDifficulty());
        trick.setLastUpdated(LocalDate.now());
        trickRepository.save(trick);
        trickRepository.deleteById(update.getId());
    }

    public void removeUpdatedTrick(Trick trick) {
        Trick updated = trickRepository.findByUpdated(trick.getId())
            .orElseThrow(() -> new ServiceException("Couldn't find updated trick!"));
        updated.setUpdatedBy(null);
        trickRepository.save(updated);
        trickRepository.deleteById(trick.getId());
    }

    public void publishCreatedTrick(Trick trick) {
        trick.setPublishStatus(PUBLISHED);
        trick.setLastUpdated(LocalDate.now());
        trickRepository.save(trick);
    }

    public List<Trick> applyUpdatedFieldsToTricks(List<Trick> tricks) {
        return tricks.stream()
            .map(this::applyUpdatedFieldsToTrick)
            .toList();
    }

    public Trick applyUpdatedFieldsToTrick(Trick trick) {
        if (!trick.getPublishStatus().equals(PUBLISHED)
            || trick.getUpdatedBy() == null) {
            return trick;
        }

        Trick updated = trick.getUpdatedBy();
        trick.setName(updated.getName());
        trick.setDifficulty(updated.getDifficulty());
        trick.setPublishStatus(updated.getPublishStatus());
        trick.setLastUpdated(updated.getLastUpdated());
        trick.setTrickParents(updated.getTrickParents());
        trick.setTrickChildren(updated.getTrickChildren());
        trick.setTrickVariants(updated.getTrickVariants());
        return trick;
    }
}
