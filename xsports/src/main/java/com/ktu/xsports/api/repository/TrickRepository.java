package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Trick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrickRepository extends JpaRepository<Trick, Long> {

}
