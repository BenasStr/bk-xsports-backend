package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.exceptions.AlreadyExistsException;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.SportRepository;
import com.ktu.xsports.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SportService {

    private final UserRepository userRepository;
    private final SportRepository sportRepository;

    public List<Sport> findSports() {
        return sportRepository.findAll();
    }

    public List<Sport> findMySports(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ServiceException("This user does not exist")
            );
        return user.getSports();
    }

    public void addSportToUserList(int sportId, String email) {
        //TODO add exceptions
        Sport sport = findSportById(sportId).orElseThrow(() ->
                new ServiceException(String.format("Sport with id %d does not exist", sportId))
            );

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ServiceException("This user does not exist")
            );

        if(user.getSports().contains(sport)) {
            return;
        }

        user.getSports().add(sport);
        userRepository.save(user);
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

    public void removeMyListSport(long sportId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ServiceException("This user doesn't exist")
            );

        Sport sport = sportRepository.findById(sportId).orElseThrow(() ->
                new ServiceException(String.format("Sport with id %d does not exist", sportId))
            );

        user.getSports().remove(sport);
        userRepository.save(user);
    }
}
