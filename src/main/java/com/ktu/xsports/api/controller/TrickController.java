package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.trick.TrickRequest;
import com.ktu.xsports.api.dto.request.trick.TrickVariantRequest;
import com.ktu.xsports.api.dto.response.trick.TrickBasicResponse;
import com.ktu.xsports.api.dto.response.trick.TrickExtendedResponse;
import com.ktu.xsports.api.service.ProgressService;
import com.ktu.xsports.api.service.trick.TrickGroupService;
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

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks")
@Slf4j
public class TrickController {
    private final TrickGroupService trickGroupService;
    private final ProgressService progressService;
    private final ModelMapper modelMapper;

    //TODO remove extended crap
    @GetMapping()
    public ResponseEntity<?> findTricks(
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @AuthenticationPrincipal User user,
        @RequestParam(defaultValue = "Standard") String variant,
        @RequestParam(defaultValue = "false") Boolean extended,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "") String publishStatus,
        @RequestParam(defaultValue = "") String difficulty,
        @RequestParam(defaultValue = "") Boolean missingVideo,
        @RequestParam(defaultValue = "") Boolean missingVariants
    ) {
        log.info("User is fetching multiple tricks.");
        List<TrickVariant> tricks = trickGroupService.findTricks(sportId, categoryId, variant, search, publishStatus, difficulty, missingVideo, missingVariants, user);

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
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long trickId,
        @AuthenticationPrincipal User user
    ) {
        log.info("User is fetching trick.");
        TrickVariant trick = trickGroupService.findTrickById(sportId, categoryId, trickId, user.getId());
        TrickExtendedResponse response = modelMapper.map(trick, TrickExtendedResponse.class);
        return ResponseEntity.ok(
            Map.of("data", response)
        );
    }

    @PostMapping()
    public ResponseEntity<?> createTrick(
        @RequestBody @Valid TrickRequest trickRequest,
        @PathVariable long categoryId,
        @PathVariable long sportId
    ) {
        log.info("User is creating trick");
        TrickVariant trick = trickRequest.toTrick();
        TrickVariant newTrick = trickGroupService.createStandardTrick(sportId, categoryId, trick);
        TrickExtendedResponse response = modelMapper.map(newTrick, TrickExtendedResponse.class);

        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(newTrick, TrickExtendedResponse.class))
        );
    }

    @PostMapping("/{trickId}/variant")
    public ResponseEntity<?> createTrickVariant(
        @RequestBody @Valid TrickVariantRequest trickVariantRequest,
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long trickId
    ) {
        log.info("User is creating trick variant");
        TrickVariant trickVariant = trickVariantRequest.toTrickVariant();
        TrickVariant newVariant = trickGroupService.createTrick(sportId, categoryId, trickId, trickVariant);

        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(newVariant, TrickBasicResponse.class))
        );
    }

    @PostMapping("/{trickVariantId}/video")
    public ResponseEntity<?> uploadTrickVideo(
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long trickVariantId,
        @RequestParam MultipartFile file
    ) {
        TrickVariant trickVariant = trickGroupService.uploadVideo(sportId, categoryId, trickVariantId, file);

        return ResponseEntity.ok(
            Map.of("data", trickVariant.getVideoUrl())
        );
    }

    @PutMapping("/{trickId}")
    public ResponseEntity<?> updateTrick(
        @RequestBody @Valid TrickRequest trickRequest,
        @PathVariable long trickId,
        @PathVariable long categoryId,
        @PathVariable long sportId
    ) {
        TrickVariant trick = trickRequest.toTrick();
        TrickVariant mainVariant = trickGroupService.updateStandardTrick(sportId, categoryId, trickId, trick);
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(mainVariant, TrickExtendedResponse.class))
        );
    }

    @PutMapping("/{trickId}/variant/{variantId}")
    public ResponseEntity<?> updateTrickVariant(
        @RequestBody @Valid TrickVariantRequest trickVariantRequest,
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long trickId,
        @PathVariable long variantId
    ) {
        TrickVariant trickVariant = trickVariantRequest.toTrickVariant();
        TrickVariant variant = trickGroupService.updateTrick(sportId, categoryId, trickId, variantId, trickVariant);
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(variant, TrickBasicResponse.class))
        );
    }

    @PutMapping("/{trickId}/progress")
    public ResponseEntity<?> updateTrickProgress (
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long trickId,
        @AuthenticationPrincipal User user
    ) {
        log.info("User is updating progress on trick!");
        TrickVariant trick = progressService.updateProgress(sportId, categoryId, trickId, user.getId());
        return ResponseEntity.ok(Map.of("data", modelMapper.map(trick, TrickExtendedResponse.class)));
    }

    @DeleteMapping("/{trickId}")
    public ResponseEntity<?> deleteTrick(
        @PathVariable long trickId,
        @PathVariable long categoryId,
        @PathVariable long sportId
    ) {
        log.info("User is deleting trick!");
        trickGroupService.removeStandardTrick(sportId, categoryId, trickId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{trickId}/variant/{variantId}")
    public ResponseEntity<?> deleteTrickVariant(
        @PathVariable long trickId,
        @PathVariable long categoryId,
        @PathVariable long sportId,
        @PathVariable long variantId
    ) {
        log.info("User is deleting trick variant!");
        trickGroupService.removeTrickVariant(sportId, categoryId, trickId, variantId);
        return ResponseEntity.noContent().build();
    }
}
