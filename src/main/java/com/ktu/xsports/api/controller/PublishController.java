package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.dto.request.PublishRequest;
import com.ktu.xsports.api.dto.response.publish.PublishAvailableCategoriesResponse;
import com.ktu.xsports.api.dto.response.publish.PublishResponse;
import com.ktu.xsports.api.dto.response.publish.PublishSportResponse;
import com.ktu.xsports.api.service.PublishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.ktu.xsports.api.util.ApiVersionPrefix.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + "/publish")
@Slf4j
public class PublishController {
    private final PublishService publishService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> getPublishes() {
        log.info("User is fetching publishes.");
        List<Publish> publishes = publishService.getPublishes();
        List<PublishResponse> response = publishes.stream()
            .map(publish -> modelMapper.map(publish, PublishResponse.class))
            .toList();

        return ResponseEntity.ok(
            Map.of("data", response)
        );
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getPublishCategories() {
        log.info("User is getting a list of publishable categories");
        List<Sport> sports = publishService.getPublishableItems();
        List<PublishAvailableCategoriesResponse> response = sports.stream()
            .map(sport -> modelMapper.map(sport, PublishAvailableCategoriesResponse.class))
            .toList();

        return ResponseEntity.ok(
            Map.of("data", response)
        );
    }

    @PostMapping()
    public ResponseEntity<?> createPublish(
        @RequestBody @Valid PublishRequest request
    ) {
        log.info("User is creating publish.");
        Publish publish = publishService.createPublish(request.toPublish());
        PublishResponse response = modelMapper.map(publish, PublishResponse.class);
        return ResponseEntity.ok(
            Map.of("data", response)
        );
    }

    @PostMapping("/now/sport/{sportId}/category/{categoryId}")
    public ResponseEntity<?> publish(
        @PathVariable long categoryId,
        @PathVariable long sportId
    ) {
        log.info("User is publishing.");
        publishService.publishByCategory(sportId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePublish(
        @RequestBody PublishRequest request,
        @PathVariable long id
    ) {
        log.info("User is updating publish.");
        Publish publish = publishService.updatePublish(request.toPublish(), id);
        PublishResponse response = modelMapper.map(publish, PublishResponse.class);
        return ResponseEntity.ok(
            Map.of("data", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublish(
        @PathVariable long id
    ) {
        log.info("User is deleting publish.");
        publishService.deletePublish(id);
        return ResponseEntity.noContent().build();
    }
}
