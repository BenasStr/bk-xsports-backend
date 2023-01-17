package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    public Optional<Sport> findByName(String name);
}
