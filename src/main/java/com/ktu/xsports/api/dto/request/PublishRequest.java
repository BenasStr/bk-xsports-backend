package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.category.IdToCategoryConverter;
import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Publish;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishRequest {
    private LocalDate releaseDate;

    @NotNull
    @JsonProperty("categoryId")
    @JsonDeserialize(converter = IdToCategoryConverter.class)
    private Category category;

    public Publish toPublish() {
        return Publish.builder()
            .category(category)
            .releaseDate(releaseDate)
            .build();
    }
}
