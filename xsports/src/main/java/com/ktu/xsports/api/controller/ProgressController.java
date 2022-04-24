package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.dto.request.ProgressRequest;
import com.ktu.xsports.api.dto.response.ProgressResponse;
import com.ktu.xsports.api.service.progress.internal.ProgressCreator;
import com.ktu.xsports.api.service.progress.internal.ProgressRemover;
import com.ktu.xsports.api.service.progress.internal.ProgressRetriever;
import com.ktu.xsports.api.service.progress.internal.ProgressUpdater;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("progress")
public class ProgressController {

    private final ProgressRetriever progressRetriever;
    private final ProgressCreator progressCreator;
    private final ProgressUpdater progressUpdater;
    private final ProgressRemover progressRemover;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findProgress(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20", name = "per_page") int size,
            @RequestParam @NotNull long userId
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Progress> progressPage = progressRetriever.findProgress(pageable, userId);
        Page<ProgressResponse> progressResponsePage = progressPage.map(
                lesson -> modelMapper.map(lesson, ProgressResponse.class)
        );

        return ResponseEntity.ok(
                PageableConverter.convert(page, size, progressResponsePage)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProgress(@PathVariable long id) {
        Optional<Progress> progress = progressRetriever.findProgressById(id);

        return ResponseEntity.of(
                progress.map(p ->
                        Map.of("data", modelMapper.map(p, ProgressResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createProgress(
            @RequestBody @Valid ProgressRequest progressRequest
    ) {
        Progress progress = progressRequest.toProgress();
        Optional<Progress> newProgress = progressCreator.createProgress(progress);

        return ResponseEntity.of(
                newProgress.map(p ->
                        Map.of("data", modelMapper.map(p, ProgressResponse.class))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProgress(
            @PathVariable long id,
            @RequestBody @Valid ProgressRequest progressRequest
    ) {
        Progress progress = progressRequest.toProgress();
        Optional<Progress> newLesson = progressUpdater.updateProgress(progress, id);

        return ResponseEntity.of(
                newLesson.map(p ->
                        Map.of("data", modelMapper.map(p, ProgressResponse.class))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProgress(@PathVariable long id) {
        Optional<Progress> deletedProgress = progressRemover.removeProgress(id);

        return ResponseEntity.of(
                deletedProgress.map(p ->
                        Map.of("data", modelMapper.map(p, ProgressResponse.class))));
    }
}
