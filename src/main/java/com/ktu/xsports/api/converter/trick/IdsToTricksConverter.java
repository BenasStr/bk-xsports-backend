package com.ktu.xsports.api.converter.trick;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@RequiredArgsConstructor
public class IdsToTricksConverter extends StdConverter<List<Long>, List<Trick>> {
    private final TrickRepository trickRepository;

    @Override
    public List<Trick> convert(List<Long> ids) {
        return ids.stream()
                .map(id -> {
                    Trick trick = trickRepository.findById(id)
                        .orElseThrow(() -> new ServiceException("Trick not found!"));
                    if (trick.getPublishStatus().equals(PUBLISHED)) {
                        if (trick.getUpdatedBy() == null) {
                            return trick;
                        }
                        return trick.getUpdatedBy();
                    }
                    return trick;
                }).toList();
    }
}
