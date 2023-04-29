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

import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Builder
@AllArgsConstructor
public class TrickSpecification implements Specification<Trick> {

    private Long categoryId;
    private String search;
    private String difficulty;
    private String publishStatus;
    private Boolean filterUpdated;
    private Boolean missingVideo;
    private Boolean missingVariants;
    private Long maxVariants;

    @Override
    public Predicate toPredicate(Root<Trick> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Trick, TrickVariant> variantJoin = root.join("trickVariants", JoinType.LEFT);

        categoryFilter(predicates, root, criteriaBuilder);
        searchFilter(predicates, root, criteriaBuilder);
        difficultyFilter(predicates, root, criteriaBuilder);
        publishStatusFilter(predicates, root, criteriaBuilder);
        missingVideoFilter();
        missingVariantsFilter();

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void categoryFilter(List<Predicate> predicates, Root<Trick> root, CriteriaBuilder criteriaBuilder) {
        Join<Trick, Category> categoryJoin = root.join("category");
        predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
    }

    private void searchFilter(List<Predicate> predicates, Root<Trick> root, CriteriaBuilder criteriaBuilder) {
        if (search != null && !search.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + search + "%"));
        }
    }

    private void difficultyFilter(List<Predicate> predicates, Root<Trick> root, CriteriaBuilder criteriaBuilder) {
        if (difficulty != null && !difficulty.isEmpty()) {
            Join<Trick, Difficulty> difficultyJoin = root.join("difficulty");
            predicates.add(criteriaBuilder.equal(difficultyJoin.get("name"), difficulty));
        }
    }

    private void publishStatusFilter(List<Predicate> predicates, Root<Trick> root, CriteriaBuilder criteriaBuilder) {
        if (publishStatus != null && !publishStatus.isEmpty()) {
            if (publishStatus.equals(UPDATED)) {
                predicates.add(criteriaBuilder.isNotNull(root.get("updatedBy")));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("publishStatus"), publishStatus));
            }
        }

        if (filterUpdated != null && filterUpdated) {
            predicates.add(criteriaBuilder.notEqual(root.get("publishStatus"), UPDATED));
        }
    }

    private void missingVideoFilter() {
//        if (missingVideo != null && missingVideo) {
//            Subquery<Boolean> subquery = query.subquery(Boolean.class);
//            Root<TrickVariant> subRoot = subquery.from(TrickVariant.class);
//            subquery.select(criteriaBuilder.isNotNull(subRoot.get("videoUrl")));
//            subquery.where(criteriaBuilder.equal(subRoot.get("trick").get("id"), root.get("id")));
//            predicates.add(criteriaBuilder.exists(subquery));
//        } else if (missingVideo != null) {
//            predicates.add(criteriaBuilder.isNull(root.get("videoUrl")));
//        }
    }

    private void missingVariantsFilter() {
//        if (missingVariants != null && missingVariants) {
//            Join<Trick, TrickVariant> variantJoin = trickJoin.join("trickVariants", JoinType.LEFT);
//            predicates.add(criteriaBuilder.lessThan(criteriaBuilder.count(variantJoin), maxVariants));
//        } else if (missingVariants != null) {
//            Join<Trick, TrickVariant> variantJoin = trickJoin.join("trickVariants", JoinType.LEFT);
//            predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(variantJoin), maxVariants));
//        }
    }
}
