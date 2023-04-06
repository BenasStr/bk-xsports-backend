package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.trick.TrickRequest;
import com.ktu.xsports.api.dto.request.trick.TrickVariantRequest;
import com.ktu.xsports.api.dto.response.trick.TrickBasicResponse;
import com.ktu.xsports.api.dto.response.trick.TrickExtendedResponse;
import com.ktu.xsports.api.service.ProgressService;
import com.ktu.xsports.api.service.media.VideoService;
import com.ktu.xsports.api.service.util.ResponseCleanerService;
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

import static com.ktu.xsports.api.util.ApiVersionPrefix.*;
import static com.ktu.xsports.api.util.Prefix.TRICK_FILE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks")
@Slf4j
public class TrickController {

    private final TrickService trickService;
    private final ProgressService progressService;
    private final ResponseCleanerService responseCleanerService;
    private final VideoService videoService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findTricks(
        @PathVariable Long categoryId,
        @PathVariable Long sportId,
        @AuthenticationPrincipal User user,
        @RequestParam(defaultValue = "all") String difficulty,
        @RequestParam(defaultValue = "Standard") String variant,
        @RequestParam(defaultValue = "false") Boolean extended,
        @RequestParam(defaultValue = "") String search
        ) {
        log.info("User is fetching multiple tricks.");
        List<TrickVariant> tricks = trickService.findTricks(sportId, categoryId, user.getId(), variant, search);
        List<?> trickResponses;
        if (extended) {
            trickResponses = tricks.stream()
                .map(trick -> modelMapper.map(trick, TrickExtendedResponse.class))
                .peek(responseCleanerService::cleanResponse)
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
        TrickVariant trick = trickService.findTrickVariantById(sportId, categoryId, trickId, user.getId());
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

    @PostMapping("/{trickId}/variants/{variantId}/video")
    public ResponseEntity<?> uploadTrickVideo(
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long trickId,
        @PathVariable long variantId,
        @RequestParam MultipartFile file
    ) {
        TrickVariant trickVariant = trickService.findTrickVariantById(sportId, categoryId, trickId);
        String fileName = trickVariant.getVideoUrl() == null || trickVariant.getVideoUrl().equals("") ?
            videoService.uploadVideo(file, TRICK_FILE+trickVariant.getId()) :
            videoService.updateVideo(file, trickVariant.getVideoUrl());
        trickVariant.setVideoUrl(fileName);
        trickService.updateVariant(sportId, categoryId, trickId, variantId, trickVariant);

        return ResponseEntity.ok(
            Map.of("data", fileName)
        );
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
        trickService.findTrickVariantById(sportId, categoryId, trickId, user.getId());
        Trick trick = progressService.updateProgress(user.getId(), trickId);
        return ResponseEntity.ok(Map.of("data", modelMapper.map(trick, TrickExtendedResponse.class)));
    }

    @DeleteMapping("/{trickId}")
    public ResponseEntity<?> deleteTrick(
            @PathVariable Long trickId,
            @PathVariable Long categoryId,
            @PathVariable Long sportId
    ) {
        log.info("User is deleting trick!");
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
        log.info("User is deleting trick variant!");
        trickService.removeTrickVariant(sportId, categoryId, trickId, variantId);
        return ResponseEntity.ok("");
    }
}
