package com.ktu.xsports.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.repository.TrickRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class IdsToTricksConverter extends StdConverter<List<Long>, List<Trick>> {

    private final TrickRepository trickRepository;

    @Override
    public List<Trick> convert(List<Long> ids) {
        return ids.stream()
                .map(id -> trickRepository
                        .findById(id)
                        .orElse(null))
                .collect(Collectors.toList());
    }
}
