package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.difficulty.IdToDifficultyConverter;
import com.ktu.xsports.api.converter.lesson.IdToLessonConverter;
import com.ktu.xsports.api.converter.trick.IdsToTricksConverter;
import com.ktu.xsports.api.domain.Difficulty;
import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.domain.Trick;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrickRequest {

    @NotNull
    private String name;

    @NotNull
    private String video;

    @NotNull
    private String description;

    @JsonProperty("lesson_id")
    @JsonDeserialize(converter = IdToLessonConverter.class)
    private Lesson lesson;

    @JsonProperty("trick_parents_ids")
    @JsonDeserialize(converter = IdsToTricksConverter.class)
    private List<Trick> trickParent;

    @JsonProperty("trick_children_ids")
    @JsonDeserialize(converter = IdsToTricksConverter.class)
    private List<Trick> trickChild;

    @JsonProperty("difficulty_id")
    @JsonDeserialize(converter = IdToDifficultyConverter.class)
    private Difficulty difficulty;

    public Trick toTrick() {
        return Trick.builder()
                .name(name)
                .video(video)
                .description(description)
                .lesson(lesson)
                .trickParents(trickParent)
                .trickChildren(trickChild)
                .difficulty(difficulty)
                .build();
    }
}
