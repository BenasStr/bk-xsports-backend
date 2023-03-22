package com.ktu.xsports.api.dto.request.trick;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.variant.IdToVariantConverter;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.Variant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrickVariantRequest {
    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Short description is required")
    private String shortDescription;

    @NotNull(message = "Variant id is required")
    @JsonProperty("variantId")
    @JsonDeserialize(converter = IdToVariantConverter.class)
    private Variant variant;

    public TrickVariant toTrickVariant() {
        return TrickVariant.builder()
            .description(description)
            .shortDescription(shortDescription)
            .variant(variant)
            .build();
    }
}
