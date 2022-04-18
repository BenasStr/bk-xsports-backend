package com.ktu.xsports.api.service.sport.internal;

import com.ktu.xsports.api.domain.Sport;

import java.util.List;
import java.util.Optional;

public interface SportRetriever {
    List<Sport> findSports();

    Optional<Sport> findSportById(long id);
}
