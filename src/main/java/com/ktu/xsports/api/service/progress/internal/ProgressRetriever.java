package com.ktu.xsports.api.service.progress.internal;

import com.ktu.xsports.api.domain.Progress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProgressRetriever {
    Page<Progress> findProgress(Pageable pageable, long userId);

    Optional<Progress> findProgressById(long id);
}
