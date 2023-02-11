package com.ktu.xsports.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/health")
@Slf4j
public class HealthController {
    private static final String RUNNING_MESSAGE = "bk-xsports-backend is RUNNING";
    @GetMapping()
    public ResponseEntity<?> health() {
        log.info("health check called.");
        return ResponseEntity.ok(Map.of("health", RUNNING_MESSAGE));
    }
}