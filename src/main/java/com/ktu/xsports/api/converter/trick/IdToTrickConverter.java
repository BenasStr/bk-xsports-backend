package com.ktu.xsports.api.converter.trick;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.repository.TrickRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToTrickConverter extends StdConverter<Long, Trick> {

    private final TrickRepository trickRepository;

    @Override
    public Trick convert(Long id) {
        return trickRepository.findById(id).orElse(null);
    }
}