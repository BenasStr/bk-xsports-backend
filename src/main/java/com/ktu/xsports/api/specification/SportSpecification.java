package com.ktu.xsports.api.specification;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;

import java.util.ArrayList;
import java.util.List;

import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;

@Builder
@AllArgsConstructor
public class SportSpecification implements Specification<Sport> {
    private String search;
    private String publishStatus;
    private boolean isBasicUser;

    @Override
    public Predicate toPredicate(Root<Sport> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

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
