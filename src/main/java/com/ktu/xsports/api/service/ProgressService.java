package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final TrickService trickService;
    private final UserService userService;
    private final StatusService statusService;

    public Trick updateProgress(Long userId, Long trickId) {
        Trick trick = trickService.findTrickById(trickId);
        User user = userService.findById(userId);

        Optional<Progress> progress = progressRepository.findByUserIdAndTrickId(userId, trickId);

        if(progress.isPresent()) {
            progress.get().setStatus(statusService.getStatusByName(StatusService.DONE));
            progressRepository.save(progress.get());
        } else {
            progressRepository.save(Progress.builder()
                .trick(trick)
                .user(user)
                .status(statusService.getStatusByName(StatusService.STARTED))
                .build());
        }

        return trickService.findTrickById(trickId);
    }
}
