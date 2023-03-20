package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.TrickVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrickVariantRepository extends JpaRepository<TrickVariant, Long> {
    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.category.id = :categoryId "
        + "AND t.id = :trickId ")
    Optional<TrickVariant> findById(Long categoryId, Long trickId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.id = :trickId "
        + "AND NOT t.variant.id = :variantId")
    List<TrickVariant> findVariants(Long trickId, Long variantId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "LEFT JOIN progress p "
        + "WHERE t.variant.name = :variantName ")
    List<TrickVariant> findAll(String variantName);

    @Query("SELECT t FROM tricks_variants t WHERE t.variant.name = 'Standard' AND t.trick.id = :trickId")
    Optional<TrickVariant> findMainVariant(Long trickId);
}
