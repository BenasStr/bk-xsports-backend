package com.ktu.xsports.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity(name = "tricks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String video;

    @NotNull
    private String description;

    @ManyToOne
    private Category category;

    @OneToOne
    private Lesson lesson;

    @ManyToMany
    @JoinTable(
            name = "tricks_references",
            joinColumns = @JoinColumn(name = "trick_child_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "trick_parent_id",
                    referencedColumnName = "id")
    )
    private List<Trick> trickParents;

    @ManyToMany(mappedBy = "trickParents")
    private List<Trick> trickChildren;
}
