package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Sport;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    Optional<Sport> findByName(String name);

    @Query("SELECT s FROM sports s WHERE s.name LIKE %:search%")
    List<Sport> findByNameContaining(String search);

    @Query("SELECT s FROM sports s WHERE s.name = :name AND s.id <> :id")
    Optional<Sport> findByNameAndNotId(String name, Long id);

    @Query("SELECT s FROM sports s WHERE s NOT IN (SELECT u.sports FROM users u WHERE u.id = :userId)")
    List<Sport> findExploreSports(long userId);
}
