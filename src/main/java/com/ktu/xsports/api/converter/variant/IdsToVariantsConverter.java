package com.ktu.xsports.api.converter.variant;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.repository.VariantRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class IdsToVariantsConverter extends StdConverter<List<Long>, List<Variant>> {

    private final VariantRepository variantRepository;

    @Override
    public List<Variant> convert(List<Long> ids) {
        return variantRepository.findByIds(ids);
    }
}
