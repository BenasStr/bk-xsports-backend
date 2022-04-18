package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.IdToSportConverter;
import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Sport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotNull
    private String name;

    @NotNull
    private String photo;

    @NotNull
    @JsonProperty("sport_id")
    @JsonDeserialize(converter = IdToSportConverter.class)
    private Sport sport;

    public Category toCategory() {
        return Category.builder()
                .name(name)
                .photo(photo)
                .sport(sport)
                .build();
    }
}
