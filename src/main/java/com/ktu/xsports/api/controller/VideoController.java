package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/videos")
@Slf4j
public class VideoController {
    private final VideoService videoService;

    @GetMapping("/{fileName}")
    public ResponseEntity<?> getVideo(@PathVariable("fileName") String fileName) {
        return ResponseEntity.ok("");
    }

    @PostMapping
    public ResponseEntity<?> postVideo() {
        return ResponseEntity.ok("");
    }

    @PutMapping
    public ResponseEntity<?> updateVideo() {
        return ResponseEntity.ok("");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteVideo() {
        return ResponseEntity.ok("");
    }
}
