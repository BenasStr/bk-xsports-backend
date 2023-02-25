package com.ktu.xsports.api.converter.difficulty;

import com.ktu.xsports.api.domain.Difficulty;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class DifficultyToNameConverter extends AbstractConverter<Difficulty, String> {

    @Override
    protected String convert(Difficulty difficulty) {
        return difficulty.getName();
    }
}
