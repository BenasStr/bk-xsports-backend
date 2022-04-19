package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.dto.request.LessonRequest;
import com.ktu.xsports.api.dto.response.LessonResponse;
import com.ktu.xsports.api.service.lesson.internal.LessonCreator;
import com.ktu.xsports.api.service.lesson.internal.LessonRemover;
import com.ktu.xsports.api.service.lesson.internal.LessonRetriever;
import com.ktu.xsports.api.service.lesson.internal.LessonUpdater;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("lessons")
public class LessonController {

    private final LessonRetriever lessonRetriever;
    private final LessonCreator lessonCreator;
    private final LessonUpdater lessonUpdater;
    private final LessonRemover lessonRemover;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findLessons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20", name = "per_page") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Lesson> lessonsPage = lessonRetriever.findLessons(pageable);
        Page<LessonResponse> lessonResponsesPage = lessonsPage.map(
                lesson -> modelMapper.map(lesson, LessonResponse.class)
        );

        return ResponseEntity.ok(
                PageableConverter.convert(page, size, lessonResponsesPage)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findLesson(@PathVariable long id) {
        Optional<Lesson> category = lessonRetriever.findLessonById(id);

        return ResponseEntity.of(
                category.map(l -> Map.of("data", modelMapper.map(l, LessonResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createLesson(
            @RequestBody @Valid LessonRequest lessonRequest
    ) {
        Lesson lesson = lessonRequest.toLesson();
        Optional<Lesson> newCategory = lessonCreator.createLesson(lesson);

        return ResponseEntity.of(
                newCategory.map(l -> Map.of("data", modelMapper.map(l, LessonResponse.class))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(
            @PathVariable long id,
            @RequestBody @Valid LessonRequest lessonRequest
    ) {
        Lesson lesson = lessonRequest.toLesson();
        Optional<Lesson> newCategory = lessonUpdater.updateLesson(lesson, id);

        return ResponseEntity.of(
                newCategory.map(l -> Map.of("data", modelMapper.map(l, LessonResponse.class))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable long id) {
        Optional<Lesson> deletedLesson = lessonRemover.removeLesson(id);

        return ResponseEntity.of(
                deletedLesson.map(l ->
                        Map.of("data", modelMapper.map(l, LessonResponse.class))));
    }
}
