package com.ktu.xsports.api.converter.trick;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import com.ktu.xsports.api.service.trick.TrickGroupService;
import com.ktu.xsports.api.service.trick.TrickVariantService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToTrickConverter extends StdConverter<Long, Trick> {

    private final TrickVariantRepository trickVariantRepository;

    @Override
    public Trick convert(Long id) {
        return trickVariantRepository.findById(id)
            .orElseThrow(() -> new ServiceException(String.format("Trick with id: %d not found.", id)))
            .getTrick();
    }
}
