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
    long baseVariantId;
    String name;
    String shortDescription;
    String description;
    String videoUrl;
    int variantId;
    String status;
    String difficulty;
}