package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    @Query("SELECT p FROM progress p WHERE p.user.id = :userId AND p.trickVariant.id = :trickVariantId ")
    Optional<Progress> findTrickAndUserId(Long trickVariantId, Long userId);

    @Query("SELECT p FROM progress p WHERE p.user.id = :userId ")
    List<Progress> findProgressByUser(long userId);
}
