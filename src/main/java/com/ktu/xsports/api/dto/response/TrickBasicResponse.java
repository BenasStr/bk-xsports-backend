package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrickBasicResponse {
    long id;
    String name;
    String shortDescription;
    String status;
    String difficulty;
    long categoryId;
}
