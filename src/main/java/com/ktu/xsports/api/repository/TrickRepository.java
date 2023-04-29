package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrickRepository extends JpaRepository<Trick, Long>, JpaSpecificationExecutor<Trick> {
//    @Query("SELECT t FROM tricks t LEFT JOIN progress p ON p.user.id = :userId WHERE t.category.id = :categoryId")
//    List<Trick> findAll(Long userId, Long categoryId);
//
//    @Query("SELECT t FROM tricks t LEFT JOIN progress p ON p.user.id = :userId WHERE t.category.id = :categoryId AND t.difficulty = :difficultyId")
//    List<Trick> findAll(Long userId, Long categoryId, String difficultyId);

    @Query(""
        + "SELECT t FROM tricks t "
        + "WHERE t.category.id = :categoryId "
        + "AND t.id = :trickId ")
    Optional<Trick> findById(Long categoryId, Long trickId);

    @Query("SELECT t FROM tricks t LEFT JOIN progress p ON p.user.id = :userId WHERE t.category.id = :categoryId AND t.id = :trickId")
    Optional<Trick> findById(Long categoryId, Long trickId, Long userId);

    Optional<Trick> findById(long id);

    @Query("SELECT t FROM tricks t "
        + "INNER JOIN FETCH tricks_variants tv "
        + "WHERE tv.variant.id = :variantId AND tv.trick.id IN :trickId")
    List<Trick> findByVariantIdAndTrickIds(Long variantId, List<Long> trickId);

    Optional<Trick> findByName(String name);

    @Query(""
        + "SELECT t FROM tricks t "
        + "WHERE t.updatedBy.id = :updatedById ")
    Optional<Trick> findByUpdated(long updatedById);
}
