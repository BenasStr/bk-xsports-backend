package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.Category;
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

    public Category toCategory() {
        return Category.builder()
                .name(name)
                .photo(photo)
                .build();
    }
}
