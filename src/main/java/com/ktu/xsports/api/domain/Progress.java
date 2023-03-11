package com.ktu.xsports.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "progress")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Status status;

    @NotNull
    @ManyToOne
    private TrickVariant trickVariant;

    @NotNull
    @ManyToOne
    private User user;
}
