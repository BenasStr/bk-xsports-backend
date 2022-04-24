package com.ktu.xsports.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity(name = "events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String photo;

    @NotNull
    private String location;

    @NotNull
    private LocalDate date;

    @NotNull
    private boolean validated;

    @OneToMany
    private List<User> users;

    @OneToMany
    private List<Sport> sports;

}
