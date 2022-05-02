package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Trick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrickRepository extends JpaRepository<Trick, Long> {

    @Query("SELECT t FROM tricks t WHERE t.category.id = :categoryId")
    List<Trick> findAll(Long categoryId);

    @Query("SELECT t FROM tricks t WHERE t.category.id = :categoryId AND t.difficulty.name = :difficulty")
    List<Trick> findAll(Long categoryId, String difficulty);

    @Query("SELECT t FROM tricks t WHERE t.category.id = :categoryId AND t.id = :trickId")
    Optional<Trick> findById(Long categoryId, Long trickId);
}
