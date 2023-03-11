package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotNull(message = "Must have a name")
    private String name;

    @NotNull(message = "Must have a photo")
    private String photo;

    public Category toCategory() {
        return Category.builder()
                .name(name)
                .photoUrl(photo)
                .build();
    }
}
