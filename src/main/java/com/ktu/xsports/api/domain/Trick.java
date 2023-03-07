package com.ktu.xsports.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    private Difficulty difficulty;

    @OneToMany(mappedBy = "trick")
    private List<Progress> progress;

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

    @ManyToMany
    @JoinTable(
        name = "tricks_by_variants_references",
        joinColumns = @JoinColumn(name = "trick_variant_child_id",
            referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "trick_variant_parent_id",
            referencedColumnName = "id")
    )
    private List<Trick> trickVariantParent;

    @ManyToMany(mappedBy = "trickVariantParent")
    private List<Trick> trickVariantChild;
}
