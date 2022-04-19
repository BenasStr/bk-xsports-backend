package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LessonResponse {
    long id;
    String name;
    String video;
    int rating;
    String description;
    String photo;
}
