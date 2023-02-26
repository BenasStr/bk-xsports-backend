package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TrickResponse {
    long id;
    String name;
    String video;
    String description;
    long categoryId;
    long lessonId;
    String difficulty;
    String status;
    List<Long> trickParentsIds;
    List<Long> trickChildrenIds;
}