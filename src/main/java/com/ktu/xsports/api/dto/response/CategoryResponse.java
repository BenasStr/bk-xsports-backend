package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CategoryResponse {
    long id;
    String name;
    String photo;
    long sportId;
    String publishStatus;
    int tricksCount;
    LocalDate lastUpdated;
}
