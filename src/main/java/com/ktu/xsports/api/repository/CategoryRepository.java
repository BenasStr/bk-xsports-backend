package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Category;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllBySportId(long sport);

    @Query(""
        + "SELECT c FROM categories c "
        + "WHERE c.sport.id = :sportId "
        + "AND c.name "
        + "LIKE %:search% ")
    List<Category> findByNameContaining(long sportId, String search);

    Optional<Category> findBySportIdAndId(long sportId, long id);

    @Query("SELECT c FROM categories c WHERE c.name = :name AND c.sport.id = :sportId")
    Optional<Category> findCategoryWithName(String name, Long sportId);

    @Query("SELECT c FROM categories c WHERE c.name = :name AND c.sport.id = :sportId AND c.id <> :categoryId")
    Optional<Category> findCategoryWithName(String name, Long sportId, Long categoryId);
}
