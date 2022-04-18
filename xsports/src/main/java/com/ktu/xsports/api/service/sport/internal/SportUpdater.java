package com.ktu.xsports.api.service.sport.internal;

import com.ktu.xsports.api.domain.Sport;

import java.util.Optional;

public interface SportUpdater {
    Optional<Sport> updateSport(Sport sport, long id);
}
