package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.Sport;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportRequest {
    @NotNull
    private String name;

    @NotNull
    private String photo;

    public Sport toSport() {
        return Sport.builder()
                .name(name)
                .photo(photo)
                .build();
    }
}
