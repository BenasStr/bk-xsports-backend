package com.ktu.xsports.api.converter;

import com.ktu.xsports.api.domain.Trick;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TricksToIdsConverter extends AbstractConverter<List<Trick>, List<Long>> {
    @Override
    protected List<Long> convert(List<Trick> tricks) {
        return tricks.stream()
                .map(Trick::getId)
                .collect(Collectors.toList());
    }
}
