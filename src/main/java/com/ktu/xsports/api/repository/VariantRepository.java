package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant, Long> {
//    List<Variant> findAllBySportsId(long sportId);
}
