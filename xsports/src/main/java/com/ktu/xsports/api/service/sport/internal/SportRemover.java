package com.ktu.xsports.api.service.sport.internal;

import com.ktu.xsports.api.domain.Sport;

import java.util.Optional;

public interface SportRemover {
    Optional<Sport> removeSport(long id);
}
