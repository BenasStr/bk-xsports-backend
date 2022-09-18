package com.ktu.xsports.api.service.progress.internal;

import com.ktu.xsports.api.domain.Progress;

import java.util.Optional;

public interface ProgressCreator {
    Optional<Progress> createProgress(Progress progress);
}
