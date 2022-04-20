package com.ktu.xsports.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity(name = "tricks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trick {
 //situs pakeisti
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String photo;

    @ManyToOne
    private Sport sport;
}
