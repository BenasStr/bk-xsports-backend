package com.ktu.xsports.api.service.sport.internal;

import com.ktu.xsports.api.domain.sport.Sport;

import java.util.Optional;

public interface SportRemover {
    Optional<Sport> removeSport(long id);
}
