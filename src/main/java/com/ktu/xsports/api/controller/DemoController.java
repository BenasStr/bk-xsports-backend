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
@RequestMapping("api/demo")
@Slf4j
public class DemoController {
    @GetMapping()
    public ResponseEntity<?> demo() {
        return ResponseEntity.ok(Map.of("data", "This is demo"));
    }
}
