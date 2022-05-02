package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DifficultyRepository extends JpaRepository<Difficulty, Long> {

}
