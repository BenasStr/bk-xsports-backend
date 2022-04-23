package com.ktu.xsports.api.controller;


import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.dto.request.TrickRequest;
import com.ktu.xsports.api.dto.response.LessonResponse;
import com.ktu.xsports.api.dto.response.TrickResponse;
import com.ktu.xsports.api.service.trick.internal.TrickCreator;
import com.ktu.xsports.api.service.trick.internal.TrickRemover;
import com.ktu.xsports.api.service.trick.internal.TrickRetriever;
import com.ktu.xsports.api.service.trick.internal.TrickUpdater;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("tricks")
public class TrickController {

    private final TrickRetriever trickRetriever;
    private final TrickCreator trickCreator;
    private final TrickUpdater trickUpdater;
    private final TrickRemover trickRemover;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findTricks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20", name = "per_page") int size,
            @RequestParam long categoryId
            ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Trick> tricksPage = trickRetriever.findTricks(pageable, categoryId);
        Page<TrickResponse> tricksResponsesPage = tricksPage.map(
                trick -> modelMapper.map(trick, TrickResponse.class)
        );

        return ResponseEntity.ok(
                PageableConverter.convert(page, size, tricksResponsesPage)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findLesson(@PathVariable long id) {
        Optional<Trick> trick = trickRetriever.findTrickById(id);

        return ResponseEntity.of(
                trick.map(t -> Map.of("data", modelMapper.map(t, TrickResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createLesson(
            @RequestBody @Valid TrickRequest trickRequest
    ) {
        Trick trick = trickRequest.toTrick();
        Optional<Trick> newTrick = trickCreator.createTrick(trick);

        return ResponseEntity.of(
                newTrick.map(t -> Map.of("data", modelMapper.map(t, TrickResponse.class))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(
            @PathVariable long id,
            @RequestBody @Valid TrickRequest trickRequest
    ) {
        Trick trick = trickRequest.toTrick();
        Optional<Trick> newTrick = trickUpdater.updateTrick(trick, id);

        return ResponseEntity.of(
                newTrick.map(t -> Map.of("data", modelMapper.map(t, LessonResponse.class))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable long id) {
        Optional<Trick> deletedLesson = trickRemover.removeTrick(id);

        return ResponseEntity.of(
                deletedLesson.map(
                        t -> Map.of("data", modelMapper.map(t, TrickResponse.class))));
    }

}
