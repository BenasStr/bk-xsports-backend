package com.ktu.xsports.api.service.progress.internal;

import com.ktu.xsports.api.domain.Progress;

import java.util.Optional;

public interface ProgressUpdater {
    Optional<Progress> updateProgress(Progress progress, long id);
}
