package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Sport;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long>, JpaSpecificationExecutor<Sport> {
    Optional<Sport> findByName(String name);

    @Query(""
        + "SELECT s FROM sports s "
        + "WHERE s.updatedBy.id = :updatedBy ")
    Optional<Sport> findUpdatedBy(long updatedBy);

    @Query(""
        + "SELECT s FROM sports s "
        + "WHERE s.name = :name "
        + "AND s.id <> :id")
    Optional<Sport> findByNameAndNotId(String name, Long id);

    @Query(""
        + "SELECT s FROM sports s "
        + "WHERE s NOT IN (SELECT u.sports FROM users u WHERE u.id = :userId)")
    List<Sport> findExploreSports(long userId);
}
