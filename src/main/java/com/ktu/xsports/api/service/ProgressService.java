package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final UserService userService;

    public Page<Progress> findProgress(Pageable pageable, long userId) {
        return progressRepository.findAllByUserId(pageable, userId);
    }

    public List<Progress> findProgress(String email) {
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new ServiceException("User not found"));
        return progressRepository.findAllByUserId(user.getId());
    }

    public Optional<Progress> findProgressById(long id) {
        return progressRepository.findById(id);
    }

    public Optional<Progress> createProgress(Progress progress) {
        return Optional.of(progressRepository.save(progress));
    }

    public Optional<Progress> updateProgress(Progress progress, long id) {
        progress.setId(id);
        if(progressRepository.findById(id).isPresent()) {
            return Optional.of(progressRepository.save(progress));
        }
        return Optional.empty();
    }

    public Optional<Progress> removeProgress(long id) {
        Optional<Progress> deletedProgress = progressRepository.findById(id);
        if(deletedProgress.isPresent()) {
            progressRepository.delete(deletedProgress.get());
            return deletedProgress;
        }
        return Optional.empty();
    }
}