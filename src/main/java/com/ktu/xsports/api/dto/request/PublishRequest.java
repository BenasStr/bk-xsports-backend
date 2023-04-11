package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.trick.IdToTrickVariantConverter;
import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.domain.TrickVariant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishRequest {

    @NotNull
    private String name;

    private LocalDate releaseDate;

    @NotNull
    @JsonProperty("trickVariantId")
    @JsonDeserialize(converter = IdToTrickVariantConverter.class)
    private TrickVariant trickVariant;

    public Publish toPublish() {
        return Publish.builder()
            .name(name)
            .category(trickVariant.getTrick().getCategory())
            .releaseDate(releaseDate)
            .trickVariant(trickVariant)
            .build();
    }
}
