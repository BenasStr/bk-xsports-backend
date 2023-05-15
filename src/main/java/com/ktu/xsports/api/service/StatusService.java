package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Status;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public final static String PLANNING = "Planning";
    public final static String DONE = "Done";
    public final static String STARTED = "Started";

    public Status getStatusByName(String name) {
        return statusRepository.findByName(name)
            .orElseThrow(() -> new ServiceException(String.format("There's no status with name: %s", name)));
    }

}
