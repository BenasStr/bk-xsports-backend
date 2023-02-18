package com.ktu.xsports.api.converter.trick;

import com.ktu.xsports.api.domain.Progress;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProgressToStatusConverter extends AbstractConverter<List<Progress>, String> {

    @Override
    public String convert(List<Progress> progress) {
        if(progress.isEmpty()) {
            return null;
        }
        return progress.stream()
            .findFirst()
            .get()
            .getStatus()
            .getName();
    }
}
