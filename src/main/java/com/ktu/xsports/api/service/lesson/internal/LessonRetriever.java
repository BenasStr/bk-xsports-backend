package com.ktu.xsports.api.service.lesson.internal;

import com.ktu.xsports.api.domain.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LessonRetriever {
    Page<Lesson> findLessons(Pageable pageable);

    Optional<Lesson> findLessonById(long id);
}
