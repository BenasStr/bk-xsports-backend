package com.ktu.xsports.api.dto.response.publish;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class PublishResponse {
    long id;
    String name;
    LocalDate releaseDate;
    PublishSportResponse sport;
}