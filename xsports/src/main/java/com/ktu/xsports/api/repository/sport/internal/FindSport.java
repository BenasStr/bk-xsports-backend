package com.ktu.xsports.api.repository.sport.internal;

import com.ktu.xsports.api.domain.sport.Sport;

import java.util.List;
import java.util.Optional;

public interface FindSport {

    List<Sport> findAll();

    Optional<Sport> findById(long id);
}
