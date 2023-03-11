package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;
    public Optional<Progress> findTrickProgressByUser(Long trickVariantId, Long userId) {
        return progressRepository.findTrickAndUserId(trickVariantId, userId);
    }


    public Trick updateProgress(Long userId, Long trickId) {
        //TODo fix this!!!
        return Trick.builder().build();
//        Trick trick = trickService.findTrickById(trickId);
//        User user = userService.findById(userId);
//
//        Optional<Progress> progress = progressRepository.findByUserIdAndTrickId(userId, trickId);
//
//        if(progress.isPresent()) {
//            progress.get().setStatus(statusService.getStatusByName(StatusService.DONE));
//            progressRepository.save(progress.get());
//        } else {
//            progressRepository.save(Progress.builder()
//                .trickVariant(trick)
//                .user(user)
//                .status(statusService.getStatusByName(StatusService.STARTED))
//                .build());
//        }
//
//        return trickService.findTrickById(trickId);
    }
}
