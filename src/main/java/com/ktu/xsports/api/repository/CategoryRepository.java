package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllBySportId(long sport);

    Optional<Category> findBySportIdAndId(long sportId, long id);
}
