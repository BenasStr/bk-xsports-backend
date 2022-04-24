package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Trick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrickRepository extends JpaRepository<Trick, Long> {
    Page<Trick> findAllByCategoryId(Pageable pageable, long category_id);
}
