package com.ktu.xsports.api.converter.status;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.Status;
import com.ktu.xsports.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToStatusConverter extends StdConverter<Long, Status> {

    private final StatusRepository statusRepository;

    @Override
    public Status convert(Long id) {
        return statusRepository.findById(id).orElse(null);
    }
}
