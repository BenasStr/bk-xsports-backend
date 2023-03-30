package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.service.media.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        log.info(String.format("Sending video: %s", fileName));
        byte[] video = videoService.getVideo(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(video.length);
        return new ResponseEntity<>(video, headers, HttpStatus.OK);
    }
}
