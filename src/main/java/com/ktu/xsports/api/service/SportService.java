package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.advice.exceptions.AlreadyExistsException;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.repository.SportRepository;
import com.ktu.xsports.api.repository.UserRepository;
import com.ktu.xsports.api.service.media.ImageService;
import com.ktu.xsports.api.specification.SportSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.ktu.xsports.api.util.PublishStatus.NOT_PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;
import static com.ktu.xsports.api.util.Role.*;

@Service
@RequiredArgsConstructor
public class SportService {
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final SportRepository sportRepository;

    public List<Sport> findSports(String search, String publishStatus, User user) {
        SportSpecification spec;
        if(user.getRole().equals(USER)) {
            spec = new SportSpecification(search, PUBLISHED, true);
        } else {
            spec = new SportSpecification(search, publishStatus, false);
        }

        return sportRepository.findAll(spec);
    }

    public List<Sport> findMySports(User user)
    {
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new ServiceException("User does not exist."))
            .getSports();
        //TODO this will be hard to update.
    }

    public List<Sport> findExploreSports(User userId) {
        SportSpecification spec;

        if (userId.getRole().equals(USER)) {
            spec = SportSpecification.builder()
                .isBasicUser(true)
                .build();
        }
        return sportRepository.findExploreSports(userId.getId());
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
        sport.setPublishStatus(NOT_PUBLISHED);
        sport.setLastUpdated(LocalDate.now());
        return sportRepository.save(sport);
    }

    @Transactional
    public Sport updateSport(Sport sport, long id) {
        Optional<Sport> existingName = sportRepository.findByNameAndNotId(sport.getName(), id);
        if (existingName.isPresent()) {
            throw new AlreadyExistsException(String.format("Sport with name %s already exists", sport.getName()));
        }

        Sport existingSport = findSportById(id);
        sport.setLastUpdated(LocalDate.now());
        if (existingSport.getPhotoUrl() != null) {
            sport.setPhotoUrl(existingSport.getPhotoUrl());
        }

        if (existingSport.getPublishStatus().equals(PUBLISHED)) {
            sport.setPublishStatus(UPDATED);
            Sport updated = sportRepository.save(sport);
            existingSport.setUpdatedBy(updated);
            sportRepository.save(existingSport);
            return sportRepository.findById(updated.getId())
                .orElseThrow(() -> new ServiceException("Failed updating sport!"));
        }

        sport.setId(id);
        sport.setPublishStatus(existingSport.getPublishStatus());
        return sportRepository.save(sport);
    }

    @Transactional
    public void removeSport(long id) {
        Sport sport = findSportById(id);

        if (sport.getPublishStatus().equals(UPDATED)) {
            Optional<Sport> updated = sportRepository.findUpdatedBy(sport.getId());
            if(updated.isPresent()) {
                updated.get().setUpdatedBy(null);
                sportRepository.save(updated.get());
                if(sport.getPhotoUrl() != null &&
                    updated.get().getUpdatedBy() != null &&
                    !sport.getPhotoUrl().equals(updated.get().getPhotoUrl())) {
                    imageService.deleteImage(sport.getPhotoUrl());
                }
            }
        } else if (sport.getUpdatedBy() != null) {
            sportRepository.findById(sport.getId())
                .ifPresent(updatedBy -> {
                    updatedBy.setPublishStatus(PUBLISHED);
                    sportRepository.save(updatedBy);
                });
        }

        if (sport.getPhotoUrl() != null) {
            imageService.deleteImage(sport.getPhotoUrl());
        }

        sportRepository.delete(sport);
    }

    public void removeMyListSport(long sportId, User user) {
        Sport sport = sportRepository.findById(sportId).orElseThrow(() ->
                new ServiceException(String.format("Sport with id %d does not exist", sportId))
            );

        user.getSports().remove(sport);
        userRepository.save(user);
    }
}
