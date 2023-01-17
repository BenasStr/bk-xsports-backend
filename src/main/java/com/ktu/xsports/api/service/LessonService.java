package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.repository.LessonsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonsRepository lessonsRepository;

    public Page<Lesson> findLessons(Pageable pageable) {
        return lessonsRepository.findAll(pageable);
    }

    public Optional<Lesson> findLessonById(long id) {
        return lessonsRepository.findById(id);
    }

    public Optional<Lesson> createLesson(Lesson lesson) {
        return Optional.of(lessonsRepository.save(lesson));
    }

    public Optional<Lesson> updateLesson(Lesson lesson, long id) {
        lesson.setId(id);
        if(lessonsRepository.findById(id).isPresent()) {
            return Optional.of(lessonsRepository.save(lesson));
        }
        return Optional.empty();
    }

    public Optional<Lesson> removeLesson(long id) {
        Optional<Lesson> deletedLesson = lessonsRepository.findById(id);
        if(deletedLesson.isPresent()) {
            lessonsRepository.delete(deletedLesson.get());
            return deletedLesson;
        }
        return Optional.empty();
    }
}
