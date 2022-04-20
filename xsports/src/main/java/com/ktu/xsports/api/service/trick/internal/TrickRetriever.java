package com.ktu.xsports.api.service.trick.internal;

import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.domain.Trick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TrickRetriever {
    Page<Trick> findTricks(Pageable pageable, long sportId, long categoryId);

    Optional<Lesson> findTrickById(long id);
}
