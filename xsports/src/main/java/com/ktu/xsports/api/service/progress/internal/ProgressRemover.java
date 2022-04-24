package com.ktu.xsports.api.service.progress.internal;

import com.ktu.xsports.api.domain.Progress;

import java.util.Optional;

public interface ProgressRemover {
    Optional<Progress> removeProgress(long id);
}
