package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.trick.TrickRequest;
import com.ktu.xsports.api.dto.response.TrickBasicResponse;
import com.ktu.xsports.api.dto.response.TrickResponse;
import com.ktu.xsports.api.service.ProgressService;
import com.ktu.xsports.api.service.TrickService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TrickController {

    private final TrickService trickService;
    private final ProgressService progressService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findTricks(
        @PathVariable Long categoryId,
        @PathVariable Long sportId,
        @AuthenticationPrincipal User user,
        @RequestParam(defaultValue = "all") String difficulty
    ) {
        log.info("User is fetching multiple tricks.");
        List<TrickVariant> tricks = trickService.findTricks(sportId, categoryId, difficulty, user.getId());
        List<TrickBasicResponse> trickResponses = tricks.stream()
            .map(trick -> modelMapper.map(trick, TrickBasicResponse.class))
            .toList();

        return ResponseEntity.ok(
            Map.of("data", trickResponses)
        );
    }

    @GetMapping("/{trickId}")
    public ResponseEntity<?> findTricksById(
        @PathVariable Long categoryId,
        @PathVariable Long sportId,
        @PathVariable Long trickId,
        @AuthenticationPrincipal User user
    ) {
        log.info("User is fetching trick.");
        TrickVariant trick = trickService.findTrickById(sportId, categoryId, trickId, user.getId());
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(trick, TrickResponse.class))
        );
    }

    @PostMapping()
    public ResponseEntity<?> createTrick(
            @RequestBody @Valid TrickRequest trickRequest,
            @PathVariable long categoryId,
            @PathVariable long sportId
    ) {
        log.info("User is creating tricks");
        Trick trick = trickRequest.toTrick();
        TrickVariant mainVariant = trickService.createTrick(sportId, categoryId, trick);
        return ResponseEntity.ok(
                Map.of("data", modelMapper.map(mainVariant, TrickResponse.class))
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
        TrickVariant mainVariant = trickService.updateTrick(sportId, categoryId, trick, trickId);

        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(mainVariant, TrickResponse.class))
        );
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
