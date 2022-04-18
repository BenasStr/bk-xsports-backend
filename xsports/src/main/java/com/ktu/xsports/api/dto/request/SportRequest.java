package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.sport.Sport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
