package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.service.media.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ktu.xsports.api.util.ApiVersionPrefix.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + "/images")
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getCurrentUserImage(@PathVariable("fileName") String fileName) {
        log.info(String.format("Sending image: %s", fileName));
        byte[] image = imageService.getImage(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.length);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
