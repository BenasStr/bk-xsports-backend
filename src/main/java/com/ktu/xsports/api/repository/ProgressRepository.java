package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByUserIdAndTrickId(long user_id, long trick_id);
}
