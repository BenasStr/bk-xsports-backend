package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Progress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Page<Progress> findAllByUserId(Pageable pageable, long user_id);
}
