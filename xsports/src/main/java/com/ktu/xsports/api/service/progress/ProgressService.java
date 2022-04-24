package com.ktu.xsports.api.service.progress;

import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.repository.ProgressRepository;
import com.ktu.xsports.api.service.progress.internal.ProgressCreator;
import com.ktu.xsports.api.service.progress.internal.ProgressRemover;
import com.ktu.xsports.api.service.progress.internal.ProgressRetriever;
import com.ktu.xsports.api.service.progress.internal.ProgressUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService implements ProgressRetriever, ProgressCreator, ProgressUpdater, ProgressRemover {

    private final ProgressRepository progressRepository;

    @Override
    public Page<Progress> findProgress(Pageable pageable, long userId) {
        return progressRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Optional<Progress> findProgressById(long id) {
        return progressRepository.findById(id);
    }

    @Override
    public Optional<Progress> createProgress(Progress progress) {
        return Optional.of(progressRepository.save(progress));
    }

    @Override
    public Optional<Progress> updateProgress(Progress progress, long id) {
        progress.setId(id);
        if(progressRepository.findById(id).isPresent()) {
            return Optional.of(progressRepository.save(progress));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Progress> removeProgress(long id) {
        Optional<Progress> deletedProgress = progressRepository.findById(id);
        if(deletedProgress.isPresent()) {
            progressRepository.delete(deletedProgress.get());
            return deletedProgress;
        }
        return Optional.empty();
    }
}