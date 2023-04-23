package com.ktu.xsports.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity(name = "categories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @NotNull
    @OneToMany(mappedBy = "category")
    private List<Trick> tricks;

    @OneToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Category updatedBy;
}
