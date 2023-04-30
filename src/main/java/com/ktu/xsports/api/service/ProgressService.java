package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.ProgressRepository;
import com.ktu.xsports.api.service.trick.TrickGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final TrickGroupService trickGroupService;
    private final UserService userService;
    private final StatusService statusService;

    public Optional<Progress> findTrickProgressByUser(Long trickVariantId, Long userId) {
        return progressRepository.findTrickAndUserId(trickVariantId, userId);
    }

    public List<Progress> findProgressByUser(long userId) {
        return progressRepository.findProgressByUser(userId);
    }

    public TrickVariant updateProgress(long sportId, long categoryId, long trickId, long userId) {
        TrickVariant trickVariant = trickGroupService.findTrickById(sportId, categoryId, trickId);

        User user = userService.findById(userId);

        Optional<Progress> userProgress = progressRepository.findTrickAndUserId(trickId, userId);

        if (userProgress.isPresent()) {
            if (userProgress.get().getStatus().getName().equals(StatusService.DONE)) {
                progressRepository.delete(userProgress.get());
            } else {
                userProgress.get().setStatus(statusService.getStatusByName(StatusService.DONE));
                userProgress.get().setDateLearned(LocalDate.now());
                progressRepository.save(userProgress.get());
            }
        } else {
            progressRepository.save(Progress.builder()
                .user(user)
                .trickVariant(trickVariant)
                .status(statusService.getStatusByName(StatusService.STARTED))
                .dateLearned(LocalDate.now())
                .build());
        }
        return trickGroupService.findTrickById(sportId, categoryId, trickId);
    }
}
