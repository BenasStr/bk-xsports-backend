package com.ktu.xsports.api.specification;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Sport;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@AllArgsConstructor
public class CategorySpecification implements Specification<Category> {
    private long sportId;
    private String search;
    private String publishStatus;
    private boolean isBasicUser;

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Category, Sport> sportJoin = root.join("sport");
        predicates.add(criteriaBuilder.equal(sportJoin.get("id"), sportId));

        if (search != null && !search.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + search + "%"));
        }

        if (publishStatus != null && !publishStatus.isEmpty()) {
            if (!publishStatus.equals(UPDATED)) {
                predicates.add(criteriaBuilder.equal(root.get("publishStatus"), publishStatus));
                predicates.add(criteriaBuilder.isNull(root.get("updatedBy")));
            } else {
                predicates.add(criteriaBuilder.isNotNull(root.get("updatedBy")));
            }
        }

        if (isBasicUser) {
            predicates.add(criteriaBuilder.equal(root.get("publishStatus"), PUBLISHED));
        } else {
            predicates.add(criteriaBuilder.notEqual(root.get("publishStatus"), UPDATED));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
