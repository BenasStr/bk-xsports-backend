package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Variant;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantRepository extends JpaRepository<Variant, Long> {
//    List<Variant> findAllBySportsId(long sportId);
    Optional<Variant> findByName(@NotNull String name);
}
