package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.dto.request.VariantRequest;
import com.ktu.xsports.api.dto.response.VariantResponse;
import com.ktu.xsports.api.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/variants")
@Slf4j
public class VariantController {
    private final VariantService variantService;

    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findVariants() {
        log.info("Finding variants");
        List<Variant> sports = variantService.getVariants();
        List<VariantResponse> variantResponse = sports.stream().map(
            variant -> modelMapper.map(variant, VariantResponse.class)
        ).toList();
        return ResponseEntity.ok(Map.of("data", variantResponse));
    }

    @PostMapping()
    public ResponseEntity<?> createVariant(@RequestBody @Valid VariantRequest variantRequest) {
        log.info("Create variant");
        Variant variant = variantService.createVariant(variantRequest.toVariant());
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(variant, VariantResponse.class))
        );
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<?> updateVariant(
        @RequestBody @Valid VariantRequest variantRequest,
        @PathVariable Long variantId
    ) {
        log.info("Update variant");
        Variant variant = variantService.updateVariant(variantRequest.toVariant(), variantId);
        return ResponseEntity.ok(
            Map.of("data", modelMapper.map(variant, VariantResponse.class))
        );
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<?> deleteVariant(@PathVariable Long variantId) {
        log.info("Delete variant");
        variantService.deleteVariant(variantId);
        return ResponseEntity.noContent().build();
    }
}
