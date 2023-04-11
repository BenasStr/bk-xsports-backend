package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Variant;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    Optional<Variant> findByName(@NotNull String name);

    @Query("SELECT v FROM variants v WHERE v.id IN(:ids)")
    List<Variant> findByIds(List<Long> ids);
}
