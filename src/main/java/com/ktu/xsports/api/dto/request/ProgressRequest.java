package com.ktu.xsports.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ktu.xsports.api.converter.IdToStatusConverter;
import com.ktu.xsports.api.converter.IdToTrickConverter;
import com.ktu.xsports.api.converter.IdToUserConverter;
import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Status;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressRequest {

    @NotNull
    @JsonProperty("statusId")
    @JsonDeserialize(converter = IdToStatusConverter.class)
    private Status status;

    @NotNull
    @JsonProperty("trickId")
    @JsonDeserialize(converter = IdToTrickConverter.class)
    private Trick trick;

    @NotNull
    @JsonProperty("userId")
    @JsonDeserialize(converter = IdToUserConverter.class)
    private User user;

    public Progress toProgress() {
        return Progress.builder()
                .status(status)
                .trick(trick)
                .user(user)
                .build();
    }
}