package com.ktu.xsports.api.service.lesson;

import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.repository.LessonsRepository;
import com.ktu.xsports.api.service.lesson.internal.LessonCreator;
import com.ktu.xsports.api.service.lesson.internal.LessonRemover;
import com.ktu.xsports.api.service.lesson.internal.LessonRetriever;
import com.ktu.xsports.api.service.lesson.internal.LessonUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService implements LessonRetriever, LessonCreator, LessonUpdater, LessonRemover {

    private final LessonsRepository lessonsRepository;

    @Override
    public Page<Lesson> findLessons(Pageable pageable) {
        return lessonsRepository.findAll(pageable);
    }

    @Override
    public Optional<Lesson> findLessonById(long id) {
        return lessonsRepository.findById(id);
    }

    @Override
    public Optional<Lesson> createLesson(Lesson lesson) {
        return Optional.of(lessonsRepository.save(lesson));
    }

    @Override
    public Optional<Lesson> updateLesson(Lesson lesson, long id) {
        lesson.setId(id);
        if(lessonsRepository.findById(id).isPresent()) {
            return Optional.of(lessonsRepository.save(lesson));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Lesson> removeLesson(long id) {
        Optional<Lesson> deletedLesson = lessonsRepository.findById(id);
        if(deletedLesson.isPresent()) {
            lessonsRepository.delete(deletedLesson.get());
            return deletedLesson;
        }
        return Optional.empty();
    }
}
