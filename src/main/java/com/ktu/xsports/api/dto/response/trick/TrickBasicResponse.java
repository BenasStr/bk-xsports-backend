package com.ktu.xsports.api.dto.response.trick;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrickBasicResponse {
    long id;
    String name;
    String shortDescription;
    String status;
    String difficulty;
}