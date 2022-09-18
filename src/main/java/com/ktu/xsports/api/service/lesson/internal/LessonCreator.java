package com.ktu.xsports.api.service.lesson.internal;

import com.ktu.xsports.api.domain.Lesson;

import java.util.Optional;

public interface LessonCreator {
    Optional<Lesson> createLesson(Lesson lesson);
}
