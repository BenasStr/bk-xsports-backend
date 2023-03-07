package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.TrickRequest;
import com.ktu.xsports.api.dto.response.TrickResponse;
import com.ktu.xsports.api.service.JwtService;
import com.ktu.xsports.api.service.ProgressService;
import com.ktu.xsports.api.service.TrickService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sports/{sportId}/categories/{categoryId}/tricks")
public class TrickController {

    private final TrickService trickService;
    private final ProgressService progressService;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @GetMapping()
    public ResponseEntity<?> findTricks(
            @PathVariable long categoryId,
            @PathVariable long sportId,
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "all") String difficulty) {
        List<Trick> tricks = trickService.findTricks(sportId, categoryId, difficulty, user.getEmail());

        List<TrickResponse> tricksResponses = tricks.stream().map(
                trick -> modelMapper.map(trick, TrickResponse.class)
        ).toList();

        return ResponseEntity.ok(Map.of("data", tricksResponses));
    }

    @GetMapping("/{trickId}")
    public ResponseEntity<?> findTrick(
            @PathVariable long trickId,
            @PathVariable long categoryId,
            @PathVariable long sportId,
            @RequestHeader("Authorization") String token
        ) {
        String email = jwtService.extractUsername(token);
        Optional<Trick> trick = trickService.findTrickById(sportId, categoryId, trickId, email);

        return ResponseEntity.of(
                trick.map(t -> Map.of("data", modelMapper.map(t, TrickResponse.class))));
    }

    @PostMapping()
    public ResponseEntity<?> createTrick(
            @RequestBody @Valid TrickRequest trickRequest,
            @PathVariable long categoryId,
            @PathVariable long sportId
    ) {
        Trick trick = trickRequest.toTrick();
        Trick newTrick = trickService.createTrick(sportId, categoryId, trick);

        return ResponseEntity.ok(
                Map.of("data", modelMapper.map(newTrick, TrickResponse.class))
        );
    }

    @PostMapping("/{trickId}/video")
    public ResponseEntity<?> uploadTrickVideo(
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long trickId,
        @RequestParam MultipartFile video
    ) {
        Trick trick = trickService.findTrickById(trickId);
        return ResponseEntity.ok("");
    }

    @PutMapping("/{trickId}")
    public ResponseEntity<?> updateTrick(
            @PathVariable long trickId,
            @RequestBody @Valid TrickRequest trickRequest,
            @PathVariable long categoryId,
            @PathVariable long sportId
    ) {
        Trick trick = trickRequest.toTrick();
        Optional<Trick> newTrick = trickService.updateTrick(sportId, categoryId, trick, trickId);

        return ResponseEntity.of(
                newTrick.map(t -> Map.of("data", modelMapper.map(t, TrickResponse.class))));
    }

    @PutMapping("/{trickId}/progress")
    public ResponseEntity<?> updateTrickProgress (
        @PathVariable Long categoryId,
        @PathVariable Long sportId,
        @PathVariable Long trickId,
        @AuthenticationPrincipal User user
    ) {
        trickService.findTrickById(sportId, categoryId, trickId);
        Trick trick = progressService.updateProgress(user.getId(), trickId);
        return ResponseEntity.ok(Map.of("data", modelMapper.map(trick, TrickResponse.class)));
    }

    @DeleteMapping("/{trickId}")
    public ResponseEntity<?> deleteTrick(
            @PathVariable long trickId,
            @PathVariable long categoryId,
            @PathVariable long sportId
    ) {
        Optional<Trick> deletedLesson = trickService.removeTrick(sportId, categoryId, trickId);

        return ResponseEntity.ok(Map.of("id", modelMapper.map(deletedLesson, TrickResponse.class)));
    }

}
