package com.ktu.xsports.api.repository.sport;

import com.ktu.xsports.api.repository.sport.internal.FindSport;
import com.ktu.xsports.api.domain.sport.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long>, FindSport {

}
