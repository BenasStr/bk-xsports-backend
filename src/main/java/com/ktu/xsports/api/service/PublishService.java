package com.ktu.xsports.api.service;

import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.repository.PublishRepository;
import com.ktu.xsports.api.service.trick.TrickService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishService {

    private final PublishRepository publishRepository;

    public Publish findById(long id) {
        return publishRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Publish not found!"));
    }

    public List<Publish> getPublishes() {
        return publishRepository.findAll();
    }

    public Publish createPublish(Publish publish) {
        return publishRepository.save(publish);
    }

    public void publish(long id) {
        Publish publish = findById(id);

        log.info("Publishing...");
    }

    public Publish updatePublish(Publish publish, long id) {
        findById(id);

        publish.setId(id);
        return publishRepository.save(publish);
    }

    public void deletePublish(long id) {
        Publish existing = findById(id);
        publishRepository.delete(existing);
    }
}
