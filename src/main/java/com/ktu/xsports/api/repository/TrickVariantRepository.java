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
        + "AND t.id = :trickId "
        + "AND t.trick.category.id = :categoryId "
        + "AND t.trick.category.sport.id = :sportId ")
    Optional<TrickVariant> findById(Long trickId, Long sportId, Long categoryId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.id = :trickId "
        + "AND NOT t.variant.id = :variantId")
    List<TrickVariant> findVariants(Long trickId, Long variantId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.variant.name = :variantName "
        + "AND t.trick.category.sport.id = :sportId "
        + "AND t.trick.category.id = :categoryId " )
    List<TrickVariant> findAll(String variantName, Long sportId, Long categoryId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.variant.name = :variantName "
        + "AND t.trick.category.sport.id = :sportId "
        + "AND t.trick.category.id = :categoryId "
        + "AND t.trick.name LIKE %:search% " )
    List<TrickVariant> findByNameContaining(String variantName, Long sportId, Long categoryId, String search);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.category.sport.id = :sportId "
        + "AND t.trick.category.id = :categoryId "
        + "AND t.trick.id = :trickId "
        + "AND t.variant.id <> :trickVariantId ")
    List<TrickVariant> findTrickVariants(Long sportId, Long categoryId, Long trickVariantId, Long trickId);

    @Query("SELECT t FROM tricks_variants t WHERE t.variant.name = 'Standard' AND t.trick.id = :trickId")
    Optional<TrickVariant> findMainVariant(Long trickId);
}
