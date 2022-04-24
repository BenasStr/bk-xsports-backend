package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.IdToCategoryConverter;
import com.ktu.xsports.api.converter.IdToLessonConverter;
import com.ktu.xsports.api.converter.IdsToTricksConverter;
import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.domain.Trick;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty("category_id")
    @JsonDeserialize(converter = IdToCategoryConverter.class)
    private Category category;

    @JsonProperty("trick_parents_ids")
    @JsonDeserialize(converter = IdsToTricksConverter.class)
    private List<Trick> trickParent;

    @JsonProperty("trick_children_ids")
    @JsonDeserialize(converter = IdsToTricksConverter.class)
    private List<Trick> trickChild;

    public Trick toTrick() {
        return Trick.builder()
                .name(name)
                .video(video)
                .description(description)
                .lesson(lesson)
                .category(category)
                .trickParents(trickParent)
                .trickChildren(trickChild)
                .build();
    }
}
