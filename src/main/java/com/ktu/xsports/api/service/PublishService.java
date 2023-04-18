package com.ktu.xsports.api.service;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.repository.PublishRepository;
import com.ktu.xsports.api.service.trick.TrickGroupService;
import com.ktu.xsports.api.service.trick.TrickService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishService {

    private final PublishRepository publishRepository;
    private final SportService sportService;
    private final CategoryService categoryService;
    private final TrickGroupService trickGroupService;

    public Publish findById(long id) {
        return publishRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Publish not found!"));
    }

    public List<Publish> getPublishes() {
        return publishRepository.findAll();
    }

    public Publish createPublish(Publish publish) {
        publish.setName(String.format("%s - %s", publish.getCategory().getSport().getName(), publish.getCategory().getName()));
        return publishRepository.save(publish);
    }

    public Publish updatePublish(Publish publish, long id) {
        Publish existing = findById(id);
        publish.setId(existing.getId());
        publish.setName(String.format("%s - %s", publish.getCategory().getSport().getName(), publish.getCategory().getName()));
        return publishRepository.save(publish);
    }

    public void deletePublish(long id) {
        Publish existing = findById(id);
        publishRepository.delete(existing);
    }

    public List<Sport> getPublishableItems() {
        List<Sport> sports = sportService.findAll();
        sports.forEach(sport ->
            sport.setCategories(sport.getCategories().stream()
                .filter(category ->
                    category.getTricks() != null
                    && category.getTricks().stream()
                        .filter(trick -> !trick.getPublishStatus().equals(PUBLISHED))
                        .toList()
                        .size() > 0
                ).toList()
            )
        );

        return sports.stream()
            .filter(sport ->
                sport.getCategories() != null
                && sport.getCategories().size() > 0
            ).toList();
    }

    public void publish(long id) {
        Publish publish = findById(id);
        sportService.publish(publish.getCategory().getSport());
        categoryService.publish(publish.getCategory());
        trickGroupService.publish(publish.getCategory().getTricks());
    }


}
