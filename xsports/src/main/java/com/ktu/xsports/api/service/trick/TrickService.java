package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.Lesson;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.service.trick.internal.TrickCreator;
import com.ktu.xsports.api.service.trick.internal.TrickRemover;
import com.ktu.xsports.api.service.trick.internal.TrickRetriever;
import com.ktu.xsports.api.service.trick.internal.TrickUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrickService implements TrickRetriever, TrickCreator, TrickUpdater, TrickRemover {

    @Override
    public Page<Trick> findTricks(Pageable pageable, long sportId, long categoryId) {
        return null;
    }

    @Override
    public Optional<Lesson> findTrickById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Trick> createTrick(Trick trick) {
        return Optional.empty();
    }

    @Override
    public Optional<Trick> updateTrick(Trick trick, long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Trick> removeTrick(long id) {
        return Optional.empty();
    }
}
