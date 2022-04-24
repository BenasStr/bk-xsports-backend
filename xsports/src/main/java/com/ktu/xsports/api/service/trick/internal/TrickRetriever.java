package com.ktu.xsports.api.service.trick.internal;

import com.ktu.xsports.api.domain.Trick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TrickRetriever {
    Page<Trick> findTricks(Pageable pageable, long categoryId);

    Optional<Trick> findTrickById(long id);
}
