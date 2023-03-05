package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Trick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;

@Repository
public interface TrickRepository extends JpaRepository<Trick, Long> {
    @Query("SELECT t FROM tricks t LEFT JOIN progress p ON p.user.id = :userId WHERE t.category.id = :categoryId")
    List<Trick> findAll(Long userId, Long categoryId);

    @Query("SELECT t FROM tricks t LEFT JOIN progress p ON p.user.id = :userId WHERE t.category.id = :categoryId AND t.difficulty = :difficultyId")
    List<Trick> findAll(Long userId, Long categoryId, String difficultyId);

    @Query("SELECT t FROM tricks t WHERE t.category.id = :categoryId AND t.id = :trickId")
    Optional<Trick> findById(Long categoryId, Long trickId);

    @Query("SELECT t FROM tricks t LEFT JOIN progress p ON p.user.id = :userId WHERE t.category.id = :categoryId AND t.id = :trickId")
    Optional<Trick> findById(Long categoryId, Long trickId, Long userId);

    Optional<Trick> findById(long id);
}
