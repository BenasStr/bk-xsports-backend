package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Difficulty;
import com.ktu.xsports.api.service.DifficultyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/difficulties")
@Slf4j
public class DifficultyController {

    private final DifficultyService difficultyService;

    @GetMapping
    private ResponseEntity<?> findStatuses() {
        log.info("Difficulty get called.");
        List<Difficulty> difficulties = difficultyService.findDifficulties();
        return ResponseEntity.ok(Map.of("data", difficulties));
    }
}
