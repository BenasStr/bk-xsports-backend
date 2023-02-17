package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Status;
import com.ktu.xsports.api.repository.StatusRepository;
import com.ktu.xsports.api.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("status")
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    private ResponseEntity<?> findStatuses() {
        List<Status> statuses = statusService.getStatuses();
        return ResponseEntity.ok(Map.of("data", statuses));
    }
}
