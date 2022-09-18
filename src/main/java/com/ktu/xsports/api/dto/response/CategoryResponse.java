package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryResponse {
    long id;
    String name;
    String photo;
    long sportId;
}
