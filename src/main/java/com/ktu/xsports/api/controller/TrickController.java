package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.trick.TrickRequest;
import com.ktu.xsports.api.dto.request.trick.TrickVariantRequest;
import com.ktu.xsports.api.dto.response.trick.TrickBasicResponse;
import com.ktu.xsports.api.dto.response.trick.TrickExtendedResponse;
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
        @RequestParam(defaultValue = "all") String difficulty,
        @RequestParam(defaultValue = "false") Boolean extended
    ) {
        log.info("User is fetching multiple tricks.");
        List<TrickVariant> tricks = trickService.findTricks(sportId, categoryId, difficulty, user.getId());
        List<?> trickResponses;
        if (extended) {
            trickResponses = tricks.stream()
                .map(trick -> modelMapper.map(trick, TrickExtendedResponse.class))
                .toList();
        } else {
            trickResponses = tricks.stream()
                .map(trick -> modelMapper.map(trick, TrickBasicResponse.class))
                .toList();
        }

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
            Map.of("data", modelMapper.map(trick, TrickExtendedResponse.class))
        );
    }

    @PostMapping()
    public ResponseEntity<?> createTrick(
            @RequestBody @Valid TrickRequest trickRequest,
            @PathVariable long categoryId,
            @PathVariable long sportId
    ) {
        log.info("User is creating trick");
        Trick trick = trickRequest.toTrick();
        TrickVariant newTrick = trickService.createTrick(sportId, categoryId, trick);
        return ResponseEntity.ok(
            //TODO change response
            Map.of("data", modelMapper.map(newTrick, TrickExtendedResponse.class))
        );
    }

    @PostMapping("/{trickId}/variant")
    public ResponseEntity<?> createTrickVariant(
        @RequestBody @Valid TrickVariantRequest trickVariantRequest,
        @PathVariable Long categoryId,
        @PathVariable Long sportId,
        @PathVariable Long trickId
    ) {
        log.info("User is creating trick variant");
        TrickVariant trickVariant = trickVariantRequest.toTrickVariant();
        TrickVariant newVariant = trickService.createTrickVariant(sportId, categoryId, trickId, trickVariant);

        return ResponseEntity.ok(
            //TODO change response
            Map.of("data", modelMapper.map(newVariant, TrickBasicResponse.class))
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
            @PathVariable Long trickId,
            @RequestBody @Valid TrickRequest trickRequest,
            @PathVariable Long categoryId,
            @PathVariable Long sportId
    ) {
        Trick trick = trickRequest.toTrick();
        TrickVariant mainVariant = trickService.updateTrick(sportId, categoryId, trick, trickId);
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(mainVariant, TrickExtendedResponse.class))
        );
    }

    @PutMapping("/{trickId}/variant/{variantId}")
    public ResponseEntity<?> updateTrickVariant(
        @RequestBody @Valid TrickVariantRequest trickVariantRequest,
        @PathVariable Long categoryId,
        @PathVariable Long sportId,
        @PathVariable Long trickId,
        @PathVariable Long variantId
    ) {
        TrickVariant trickVariant = trickVariantRequest.toTrickVariant();
        TrickVariant variant = trickService.updateVariant(sportId, categoryId, trickId, variantId, trickVariant);
        return ResponseEntity.ok(
            //TODO change variant response.
            Map.of("data", modelMapper.map(variant, TrickExtendedResponse.class))
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
        return ResponseEntity.ok(Map.of("data", modelMapper.map(trick, TrickExtendedResponse.class)));
    }

    @DeleteMapping("/{trickId}")
    public ResponseEntity<?> deleteTrick(
            @PathVariable Long trickId,
            @PathVariable Long categoryId,
            @PathVariable Long sportId
    ) {
        trickService.removeTrick(sportId, categoryId, trickId);

        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{trickId}/variant/{variantId}")
    public ResponseEntity<?> deleteTrickVariant(
        @PathVariable Long trickId,
        @PathVariable Long categoryId,
        @PathVariable Long sportId,
        @PathVariable Long variantId
    ) {
        trickService.removeTrickVariant(sportId, categoryId, trickId, variantId);
        return ResponseEntity.ok("");
    }
}
