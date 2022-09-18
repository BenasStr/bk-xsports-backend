package com.ktu.xsports.api.dto.response;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.domain.Trick;
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
    long difficultyId;
    List<Long> trickParentsIds;
    List<Long> trickChildrenIds;
}