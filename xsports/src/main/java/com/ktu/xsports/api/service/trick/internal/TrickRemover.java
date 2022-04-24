package com.ktu.xsports.api.service.trick.internal;

import com.ktu.xsports.api.domain.Trick;

import java.util.Optional;

public interface TrickRemover {
    Optional<Trick> removeTrick(long id);
}
