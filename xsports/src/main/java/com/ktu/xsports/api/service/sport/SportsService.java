package com.ktu.xsports.api.service.sport;

import com.ktu.xsports.api.domain.Sport;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SportsService {
    Optional<Sport> createSport(Sport sport);
    Optional<Sport> removeSport(long id);
    String uploadImage(MultipartFile file);
    List<Sport> findSports();

    byte[] downloadSportImage(long sportId);

    Optional<Sport> findSportById(long id);
    Optional<Sport> updateSport(Sport sport, long id);
}
