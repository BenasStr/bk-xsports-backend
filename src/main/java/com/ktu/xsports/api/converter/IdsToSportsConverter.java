package com.ktu.xsports.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.repository.SportRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class IdsToSportsConverter extends StdConverter<List<Long>, List<Sport>> {

    private final SportRepository sportRepository;

    @Override
    public List<Sport> convert(List<Long> ids) {
        return ids.stream()
                .map(id -> sportRepository
                        .findById(id)
                        .orElse(null))
                .collect(Collectors.toList());
    }
}
