package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.Lesson;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {

    @NotNull
    private String name;

    @NotNull
    private String video;

    private String description;

    @NotNull
    private String photo;

    public Lesson toLesson() {
        return Lesson.builder()
                .name(name)
                .video(video)
                .description(description)
                .rating(0)
                .photo(photo)
                .build();
    }
}
