package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.domain.Trick;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrickRequest {

    @NotNull
    private String name;

    @NotNull
    private String video;

    @NotNull
    private String description;

    private Lesson lesson;

    private List<Trick> trickParent;

    private List<Trick> trickChild;

    public Trick toTrick() {
        return Trick.builder()
                .name(name)
                .video(video)
                .description(description)
                .lesson(lesson)
                .trickParents(trickParent)
                .trickChildren(trickChild)
                .build();
    }
}
