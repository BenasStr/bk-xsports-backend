package com.ktu.xsports.api.service.lesson.internal;

import com.ktu.xsports.api.domain.Lesson;

import java.util.Optional;

public interface LessonUpdater {
    Optional<Lesson> updateLesson(Lesson lesson, long id);
}
