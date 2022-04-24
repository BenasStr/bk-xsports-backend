package com.ktu.xsports.api.service.trick.internal;

import com.ktu.xsports.api.domain.Trick;

import java.util.Optional;

public interface TrickCreator {
    Optional<Trick> createTrick(Trick trick);
}