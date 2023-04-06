package com.ktu.xsports.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity(name = "tricks_variants")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrickVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Variant variant;

    private String videoUrl;

    @NotNull
    private String description;

    @NotNull
    private String shortDescription;

    @OneToMany(mappedBy = "trickVariant")
    private List<Progress> progress;

    @ManyToOne
    private Trick trick;
}
