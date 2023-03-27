package com.ktu.xsports.api.service;

import com.ktu.xsports.api.dto.response.trick.TrickExtendedResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseCleanerService {
    public void cleanResponse(TrickExtendedResponse response) {
        response.setTrickVariants(response.getTrickVariants()
            .stream()
            .filter(inner -> inner.getId() != response.getId())
            .toList()
        );
    }
}
