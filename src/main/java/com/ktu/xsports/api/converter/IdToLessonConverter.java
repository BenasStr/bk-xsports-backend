package com.ktu.xsports.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.repository.LessonsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToLessonConverter extends StdConverter<Long, Lesson> {

    private final LessonsRepository lessonsRepository;

    @Override
    public Lesson convert(Long id) {
        return lessonsRepository.findById(id).orElse(null);
    }
}
