package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/images")
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

    @PostMapping("/user")
    public ResponseEntity<?> uploadUserImage(
        @RequestParam("file") MultipartFile image,
        @AuthenticationPrincipal User user
    ) {
        log.info("uploading user image");
        String fileName = imageService.uploadUserImage(image, Long.toString(user.getId()));
        return ResponseEntity.ok(Map.of("data", fileName));
    }

    @PostMapping("/{prefix}/{fileName}")
    public ResponseEntity<?> uploadImage(
        @RequestParam("file") MultipartFile image,
        @PathVariable String fileName,
        @PathVariable String prefix
    ) {
        //TODO implement this.
        return ResponseEntity.ok("");
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUserProfileImage(
        @RequestParam("file") MultipartFile file,
        @AuthenticationPrincipal User user
    ) {
        log.info("User is updating profile picture");
        String fileName = imageService.updateProfileImage(file, user.getPhotoPath());
        return ResponseEntity.ok(Map.of("data", fileName));
    }

    @PutMapping("/{prefix}/{fileName}")
    public ResponseEntity<?> updateImage(@PathVariable String fileName, @PathVariable String prefix) {
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUserImage(@AuthenticationPrincipal User user) {
        log.info("User is removing profile image");
        imageService.deleteUserProfileImage(user.getPhotoPath());
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{prefix}/{fileName}")
    public ResponseEntity<?> deleteImage() {
        //TODO implement this
        return ResponseEntity.ok("");
    }
}
