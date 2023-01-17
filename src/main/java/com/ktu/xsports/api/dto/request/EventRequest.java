package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.IdsToSportsConverter;
import com.ktu.xsports.api.domain.Event;
import com.ktu.xsports.api.domain.Sport;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotNull
    private String name;

    @NotNull
    private String photo;

    @NotNull
    private String location;

    @NotNull
    private LocalDate date;

    @NotNull
    @JsonDeserialize(converter = IdsToSportsConverter.class)
    private List<Sport> sports;

    private Event event() {
        return Event.builder()
                .name(name)
                .photo(photo)
                .location(location)
                .date(date)
                .sports(sports)
                .build();
    }
}
