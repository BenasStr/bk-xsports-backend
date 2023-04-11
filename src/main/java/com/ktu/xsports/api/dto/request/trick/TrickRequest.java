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
import lombok.NoArgsConstructor;

import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrickRequest {

    @NotNull(message = "Missing name")
    private String name;

    @JsonProperty("trickParentsIds")
    @JsonDeserialize(converter = IdsToTricksConverter.class)
    private List<Trick> trickParents;

    @JsonProperty("difficultyId")
    @JsonDeserialize(converter = IdToDifficultyConverter.class)
    private Difficulty difficulty;

    @NotNull(message = "Missing description")
    private String description;

    @NotNull(message = "Missing short description")
    private String shortDescription;

    public TrickVariant toTrick() {

        return TrickVariant.builder()
            .trick(Trick.builder()
                .name(name)
                .trickParents(trickParents)
                .trickChildren(List.of())
                .trickVariants(List.of())
                .difficulty(difficulty)
                .publishStatus(NOT_PUBLISHED)
                .build())
            .description(description)
            .shortDescription(shortDescription)
            .progress(null)
            .videoUrl(null)
            .build();
    }
}
