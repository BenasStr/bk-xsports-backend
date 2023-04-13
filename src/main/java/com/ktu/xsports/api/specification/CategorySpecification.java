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

@AllArgsConstructor
public class CategorySpecification implements Specification<Category> {
    private long sportId;
    private String search;
    private String publishStatus;

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Category, Sport> sportJoin = root.join("sport");
        predicates.add(criteriaBuilder.equal(sportJoin.get("id"), sportId));

        if (search != null && !search.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + search + "%"));
        }

        if (publishStatus != null && !publishStatus.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("publishStatus"), publishStatus));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}