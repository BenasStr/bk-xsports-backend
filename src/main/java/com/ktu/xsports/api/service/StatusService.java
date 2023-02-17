package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Status;
import com.ktu.xsports.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public List<Status> getStatuses() {
        return statusRepository.findAll();
    }
}
