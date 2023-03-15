package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.exceptions.AlreadyExistsException;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.SportRepository;
import com.ktu.xsports.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Sport> findMySports(long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException("User does not exist."))
            .getSports();
    }

    public List<Sport> findExploreSports(long userId) {
        return sportRepository.findExploreSports(userId);
    }

    public void addSportToUserList(int sportId, String email) {
        Sport sport = findSportById(sportId);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ServiceException("This user does not exist")
            );

        if(user.getSports().contains(sport)) {
            return;
        }

        user.getSports().add(sport);
        userRepository.save(user);
    }

    public Sport findSportById(long id) {
        return sportRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Sport not found!"));
    }

    public Sport createSport(Sport sport) {
        Optional<Sport> existingSport = sportRepository.findByName(sport.getName());
        if(existingSport.isPresent()) {
            throw new AlreadyExistsException(String.format("Sport with name %s already exists", sport.getName()));
        }

        return sportRepository.save(sport);
    }

    public Sport updateSport(Sport sport, long id) {
        Optional<Sport> existingName = sportRepository.findByName(sport.getName());
        if (existingName.isPresent()) {
            throw new AlreadyExistsException(String.format("Sport with name %s already exists", sport.getName()));
        }

        Sport existingSport = findSportById(id);
        sport.setId(id);

        if (existingSport.getPhotoUrl() != null) {
            sport.setPhotoUrl(existingSport.getPhotoUrl());
        }

        return sportRepository.save(sport);
    }

    public void removeSport(Long id) {
        sportRepository.findById(id)
            .ifPresent(sport -> sportRepository.deleteById(id));
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
