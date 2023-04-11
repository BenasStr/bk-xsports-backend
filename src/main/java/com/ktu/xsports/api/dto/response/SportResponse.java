package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SportResponse {
    long id;
    String name;
    String photo;
    String publishStatus;
    List<VariantResponse> variants;
}
