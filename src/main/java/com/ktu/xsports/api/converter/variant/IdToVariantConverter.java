package com.ktu.xsports.api.converter.variant;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.VariantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToVariantConverter extends StdConverter<Long, Variant> {
    private final VariantRepository variantRepository;

    @Override
    public Variant convert(Long id) {
        if (id == null) {
            return variantRepository.findByName("Standard")
                .orElseThrow(() -> new ServiceException("Standard variant not found!"));
        }

        return variantRepository.findById(id)
                .orElseThrow(() -> new ServiceException("With given id does not exist!"));
    }
}
