package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonsRepository extends JpaRepository<Lesson, Long> {
}
