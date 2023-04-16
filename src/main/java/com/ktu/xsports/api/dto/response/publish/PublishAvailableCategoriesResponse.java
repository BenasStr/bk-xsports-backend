package com.ktu.xsports.api.dto.response.publish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishAvailableCategoriesResponse {
    long id;
    String name;
    List<PublishCategoryResponse> category;
}
