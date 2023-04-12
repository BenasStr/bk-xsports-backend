package com.ktu.xsports.api.converter.trick;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class IdsToTricksConverter extends StdConverter<List<Long>, List<Trick>> {

    private final TrickVariantRepository trickVariantRepository;

    @Override
    public List<Trick> convert(List<Long> ids) {
        return ids.stream()
                .map(id ->
                    trickVariantRepository.findById(id)
                        .orElseThrow(() -> new ServiceException("Trick not found!"))
                        .getTrick())
                .toList();
    }
}
