package com.ktu.xsports.api.service.sport;

import com.ktu.xsports.api.domain.sport.Sport;
import com.ktu.xsports.api.repository.sport.SportRepository;
import com.ktu.xsports.api.service.sport.internal.SportCreator;
import com.ktu.xsports.api.service.sport.internal.SportRemover;
import com.ktu.xsports.api.service.sport.internal.SportRetriever;
import com.ktu.xsports.api.service.sport.internal.SportUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SportService implements SportRetriever, SportCreator, SportUpdater, SportRemover {

    private final SportRepository sportRepository;

    @Override
    public List<Sport> findSports() {
        return sportRepository.findAll();
    }

    @Override
    public Optional<Sport> findSportById(long id) {
        return sportRepository.findById(id);
    }

    @Override
    public Optional<Sport> createSport(Sport sport) {
        return Optional.of(sportRepository.save(sport));
    }

    @Override
    public Optional<Sport> updateSport(Sport sport, long id) {
        sport.setId(id);
        if(sportRepository.findById(id).isPresent()) {
            return Optional.of(sportRepository.save(sport));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Sport> removeSport(long id) {
        Optional<Sport> deletedSport = sportRepository.findById(id);
        if(deletedSport.isPresent()) {
            sportRepository.delete(deletedSport.get());
            return deletedSport;
        }
        return Optional.empty();
    }
}
