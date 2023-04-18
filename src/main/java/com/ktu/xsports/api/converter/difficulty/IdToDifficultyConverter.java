package com.ktu.xsports.api.converter.difficulty;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Difficulty;
import com.ktu.xsports.api.repository.DifficultyRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToDifficultyConverter extends StdConverter<Long, Difficulty> {

    private final DifficultyRepository difficultyRepository;

    @Override
    public Difficulty convert(Long id) {
        return difficultyRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Difficulty doesn't exist"));
    }
}
