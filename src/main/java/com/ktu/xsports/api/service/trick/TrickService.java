package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        trick.setCategory(currentTrick.getCategory());
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
        return trickRepository.save(trick);
    }

    public Trick publishTrick(Trick trick) {
        if (trick.getPublishStatus().equals(UPDATED)) {
            //TODO add the case when object is updated. Delete published and add make this published instead.
//            Trick updatedTrick = trickRepository
            trick.setPublishStatus(PUBLISHED);

            return null;
        }

        if (trick.getPublishStatus().equals(SCHEDULED) || trick.getPublishStatus().equals(NOT_PUBLISHED)) {
            trick.setPublishStatus(PUBLISHED);
            return trickRepository.save(trick);
        }
        throw new ServiceException("Published trick got into the publish list...");
    }

    public void removeTrick(long sportId, long categoryId, long trickId) {

    }
}
