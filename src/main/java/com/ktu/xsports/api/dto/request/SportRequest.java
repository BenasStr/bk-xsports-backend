package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.variant.IdsToVariantsConverter;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.Variant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportRequest {
    @NotNull(message = "Must have a name")
    private String name;

    @JsonProperty("variantsIds")
    @JsonDeserialize(converter = IdsToVariantsConverter.class)
    private List<Variant> variants;

    public Sport toSport() {
        return Sport.builder()
            .name(name)
            .variants(variants)
            .categories(List.of())
            .users(List.of())
            .build();
    }
}
