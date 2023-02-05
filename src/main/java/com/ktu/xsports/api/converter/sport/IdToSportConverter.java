package com.ktu.xsports.api.converter.sport;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.repository.SportRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToSportConverter extends StdConverter<Long, Sport> {

    private final SportRepository sportRepository;

    @Override
    public Sport convert(Long sport_id) {
        return sportRepository.findById(sport_id).orElse(null);
    }
}
