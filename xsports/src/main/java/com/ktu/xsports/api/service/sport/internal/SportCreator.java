package com.ktu.xsports.api.service.sport.internal;

import com.ktu.xsports.api.domain.sport.Sport;

import java.util.Optional;

public interface SportCreator {
    Optional<Sport> createSport(Sport sport);
}
