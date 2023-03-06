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

    public List<Sport> findMySports(User user) {
        return user.getSports();
    }

    public List<Sport> findExploreSports(User user) {
        return sportRepository.findExploreSports(user.getId());
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

    public byte[] downloadSportImage(long sportId) {
        Sport sport = sportRepository.findById(sportId).get();
        return null; //fileStore.download(path, sport.getPhoto());
    }

    public Sport findSportById(long id) {
        return sportRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Sport not found!"));
    }

    public Optional<Sport> createSport(Sport sport) {
        sportRepository.findByName(sport.getName())
            .orElseThrow(() -> new AlreadyExistsException(String.format("Sport with name %s", sport.getName())));

        return Optional.of(sportRepository.save(sport));
    }

    public Optional<Sport> updateSport(Sport sport, long id) {
        sportRepository.findByName(sport.getName())
            .orElseThrow(() -> new AlreadyExistsException(String.format("Sport with name %s", sport.getName())));

        Sport existingSport = findSportById(id);
        sport.setId(id);

        if (existingSport.getPhoto() != null) {
            sport.setPhoto(existingSport.getPhoto());
        }

        if (sportRepository.findById(id).isPresent()) {
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
