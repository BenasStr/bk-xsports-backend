package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.exceptions.AlreadyExistsException;
import com.ktu.xsports.api.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    public List<Sport> findSports() {
        return sportRepository.findAll();
    }

    public byte[] downloadSportImage(long sportId) {
        Sport sport = sportRepository.findById(sportId).get();
        return null; //fileStore.download(path, sport.getPhoto());
    }

    public Optional<Sport> findSportById(long id) {
        return sportRepository.findById(id);
    }

    public Optional<Sport> createSport(Sport sport) {
        Optional<Sport> exists = sportRepository.findByName(sport.getName());
        if(exists.isPresent()) {
            throw new AlreadyExistsException(String.format("Sport with name %s", sport.getName()));
        }

        return Optional.of(sportRepository.save(sport));
    }

    public Optional<Sport> updateSport(Sport sport, long id) {
        Optional<Sport> exists = sportRepository.findByName(sport.getName());
        if(exists.isPresent()) {
            throw new AlreadyExistsException(String.format("Sport with name %s", sport.getName()));
        }

        sport.setId(id);
        if(sportRepository.findById(id).isPresent()) {
            return Optional.of(sportRepository.save(sport));
        }
        return Optional.empty();
    }

    public Optional<Sport> removeSport(long id) {
        Optional<Sport> deletedSport = sportRepository.findById(id);
        if(deletedSport.isPresent()) {
            sportRepository.delete(deletedSport.get());
            return deletedSport;
        }
        return Optional.empty();
    }
}
