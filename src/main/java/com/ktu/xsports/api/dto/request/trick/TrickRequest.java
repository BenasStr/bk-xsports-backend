package com.ktu.xsports.api.dto.request.trick;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.difficulty.IdToDifficultyConverter;
import com.ktu.xsports.api.converter.trick.IdsToTricksConverter;
import com.ktu.xsports.api.domain.Difficulty;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrickRequest {

    @NotNull
    private String name;

    @JsonProperty("trickParentsIds")
    @JsonDeserialize(converter = IdsToTricksConverter.class)
    private List<Trick> trickParent;

    @JsonProperty("difficultyId")
    @JsonDeserialize(converter = IdToDifficultyConverter.class)
    private Difficulty difficulty;

    @NotNull
    private String description;

    @NotNull
    private String shortDescription;

    public Trick toTrick() {
        return Trick.builder()
            .name(name)
            .trickParents(trickParent)
            .difficulty(difficulty)
            .trickVariants(
                List.of(TrickVariant.builder()
                        .description(description)
                        .shortDescription(shortDescription)
                    .build()
                )
            )
            .build();
    }
}
