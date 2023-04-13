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
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity(name = "sports")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    private String photoUrl;

    @NotNull
    private String publishStatus;

    @NotNull
    private LocalDate lastUpdated;

    @ManyToMany
    @JoinTable(
        name = "sports_variants",
        joinColumns = @JoinColumn(name = "sport_id",
            referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "variant_id",
            referencedColumnName = "id")
    )
    private List<Variant> variants;
}
