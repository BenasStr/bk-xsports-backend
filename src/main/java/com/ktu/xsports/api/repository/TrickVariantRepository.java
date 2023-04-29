package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.TrickVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrickVariantRepository extends JpaRepository<TrickVariant, Long>, JpaSpecificationExecutor<TrickVariant> {

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.category.id = :categoryId "
        + "AND t.trick.id = :trickId ")
    Optional<TrickVariant> findById(long trickId, long categoryId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.variant.name = 'Standard' "
        + "AND t.trick.id = :trickId "
        + "AND t.trick.category.id = :categoryId ")
    Optional<TrickVariant> findStandardVariantById(long trickId, long categoryId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.id = :trickId "
        + "AND NOT t.variant.id = :variantId")
    List<TrickVariant> findVariants(long trickId, long variantId);


    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.category.id = :categoryId ")
    List<TrickVariant> findByCategoryId(long categoryId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.category.sport.id = :sportId "
        + "AND t.trick.category.id = :categoryId "
        + "AND t.trick.id = :trickId "
        + "AND t.variant.id <> :trickVariantId ")
    List<TrickVariant> findTrickVariants(long sportId, long categoryId, long trickVariantId, long trickId);

    @Query(""
        + "SELECT t FROM tricks_variants t "
        + "WHERE t.trick.updatedBy.id = :trickVariantUpdatedBy "
        + "AND t.variant.name = 'Standard' ")
    Optional<TrickVariant> findTrickVariantByUpdatedBy(long trickVariantUpdatedBy);
}
