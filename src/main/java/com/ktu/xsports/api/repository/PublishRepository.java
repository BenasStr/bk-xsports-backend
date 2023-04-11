package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {

}
