package com.ktu.xsports.api.service.trick;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.repository.TrickRepository;
import com.ktu.xsports.api.service.trick.internal.TrickCreator;
import com.ktu.xsports.api.service.trick.internal.TrickRemover;
import com.ktu.xsports.api.service.trick.internal.TrickRetriever;
import com.ktu.xsports.api.service.trick.internal.TrickUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrickService implements TrickRetriever, TrickCreator, TrickUpdater, TrickRemover {

    private final TrickRepository trickRepository;

    @Override
    public Page<Trick> findTricks(Pageable pageable, long categoryId) {
        return trickRepository.findAllByCategoryId(pageable, categoryId);
    }

    @Override
    public Optional<Trick> findTrickById(long id) {
        return trickRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Trick> createTrick(Trick trick) {
        return Optional.of(trickRepository.save(trick));
    }

    @Override
    public Optional<Trick> updateTrick(Trick trick, long id) {
        trick.setId(id);
        if(trickRepository.findById(id).isPresent()) {
            return Optional.of(trickRepository.save(trick));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Trick> removeTrick(long id) {
        Optional<Trick> deletedTrick = trickRepository.findById(id);
        if(deletedTrick.isPresent()) {
            trickRepository.delete(deletedTrick.get());
            return deletedTrick;
        }
        return Optional.empty();
    }
}
