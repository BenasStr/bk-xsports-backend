package com.ktu.xsports.api.converter.trick;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.repository.TrickVariantRepository;
import io.jsonwebtoken.io.SerialException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdToTrickVariantConverter extends StdConverter<Long, TrickVariant> {

    private final TrickVariantRepository trickVariantRepository;

    @Override
    public TrickVariant convert(Long id) {
        return trickVariantRepository.findById(id)
            .orElseThrow(() -> new SerialException(String.format("Trick with id: %d not found.", id)));
    }
}