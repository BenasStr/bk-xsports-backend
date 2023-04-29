package com.ktu.xsports.api.specification;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Difficulty;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.TrickVariant;
import com.ktu.xsports.api.domain.Variant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
public class TrickVariantSpecification implements Specification<TrickVariant> {
    private Long categoryId;
    private String search;
    private String difficulty;
    private String publishStatus;
    private String variant;
    private Boolean filterUpdated;

    @Override
    public Predicate toPredicate(Root<TrickVariant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<TrickVariant, Trick> trickJoin = root.join("trick");

        predicates.add(criteriaBuilder.equal(trickJoin.get("category").get("id"), categoryId));

        if (search != null && !search.isEmpty()) {
            predicates.add(criteriaBuilder.like(trickJoin.get("name"), "%" + search + "%"));
        }

        if (difficulty != null && !difficulty.isEmpty()) {
            Join<Trick, Difficulty> difficultyJoin = trickJoin.join("difficulty");
            predicates.add(criteriaBuilder.equal(difficultyJoin.get("name"), difficulty));
        }

        if (publishStatus != null && !publishStatus.isEmpty()) {
            predicates.add(criteriaBuilder.equal(trickJoin.get("publishStatus"), publishStatus));
        }

        if (variant != null && !variant.isEmpty()) {
            Join<TrickVariant, Variant> variantJoin = root.join("variant");
            predicates.add(criteriaBuilder.equal(variantJoin.get("name"), variant));
        }

        if (filterUpdated) {
            predicates.add(criteriaBuilder.isNull(trickJoin.get("updatedBy")));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}