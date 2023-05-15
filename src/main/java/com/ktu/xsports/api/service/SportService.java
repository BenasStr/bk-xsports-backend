package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.repository.SportRepository;
import com.ktu.xsports.api.repository.UserRepository;
import com.ktu.xsports.api.service.media.ImageService;
import com.ktu.xsports.api.specification.SportSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

import static com.ktu.xsports.api.util.Prefix.SPORT_FILE;
import static com.ktu.xsports.api.util.PublishStatus.DELETED;
import static com.ktu.xsports.api.util.PublishStatus.NOT_PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.PUBLISHED;
import static com.ktu.xsports.api.util.PublishStatus.SCHEDULED;
import static com.ktu.xsports.api.util.PublishStatus.UPDATED;
import static com.ktu.xsports.api.util.Role.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SportService {
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final SportRepository sportRepository;
    private final UserService userService;

    public List<Sport> findAll() {
        List<Sport> sports = sportRepository.findAll();
        return applyUpdatedFieldsToSports(sports);
    }

    public List<Sport> findSports(String search, String publishStatus, User user) {
        SportSpecification spec;
        if(user.getRole().equals(USER)) {
            spec = new SportSpecification(search, PUBLISHED, true);
            return sportRepository.findAll(spec);
        }
        spec = new SportSpecification(search, publishStatus.toUpperCase(), false);
        List<Sport> sports = sportRepository.findAll(spec);
        return applyUpdatedFieldsToSports(sports);
    }

    public List<Sport> findMySports(long userId)
    {
        User user = userService.findById(userId);

        if (user.getRole().equals(USER)) {
            return user.getSports()
                .stream()
                .filter(sport -> sport.getPublishStatus().equals(PUBLISHED))
                .toList();
        }
        List<Sport> sports = user.getSports()
            .stream()
            .filter(sport -> !sport.getPublishStatus().equals(UPDATED))
            .toList();
        return applyUpdatedFieldsToSports(sports);
    }

    public List<Sport> findExploreSports(long userId) {
        User user = userService.findById(userId);

        if (user.getRole().equals(USER)) {
            return sportRepository.findAll()
                .stream()
                .filter(sport ->
                    sport.getPublishStatus().equals(PUBLISHED)
                    && !user.getSports().contains(sport))
                .toList();
        }

        List<Sport> sports = sportRepository.findAll().stream()
            .filter(sport ->
                !sport.getPublishStatus().equals(UPDATED)
                && !user.getSports().contains(sport))
            .toList();
        return applyUpdatedFieldsToSports(sports);
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
        sport.setPublishStatus(NOT_PUBLISHED);
        sport.setLastUpdated(LocalDate.now());
        return sportRepository.save(sport);
    }

    public Sport updateSport(Sport sport, long id) {
        Sport existingSport = findSportById(id);
        sport.setLastUpdated(LocalDate.now());

        if (existingSport.getPhotoUrl() != null) {
            sport.setPhotoUrl(existingSport.getPhotoUrl());
        }

        if (existingSport.getPublishStatus().equals(PUBLISHED)) {
            if (existingSport.getUpdatedBy() == null) {
                return updatePublishedSport(sport, existingSport);
            }
            return updateUpdatedSport(sport, existingSport);
        }

        sport.setId(id);
        sport.setPublishStatus(existingSport.getPublishStatus());
        return sportRepository.save(sport);
    }

    @Transactional
    private Sport updatePublishedSport(Sport sport, Sport published) {
        sport.setPublishStatus(UPDATED);
        sport = sportRepository.save(sport);
        published.setUpdatedBy(sport);
        return sportRepository.save(published);
    }

    private Sport updateUpdatedSport(Sport sport, Sport updated) {
        sport.setId(updated.getUpdatedBy().getId());
        sport.setPublishStatus(UPDATED);
        return sportRepository.save(sport);
    }

    public Sport updateSportImage(long id, MultipartFile image) {
        Sport sport = findSportById(id);
        String fileName = sport.getPhotoUrl() == null || sport.getPhotoUrl().equals("") ?
            imageService.uploadImage(image, SPORT_FILE+sport.getId()) :
            imageService.updateImage(image, sport.getPhotoUrl());
        sport.setPhotoUrl(fileName);
        return sportRepository.save(sport);
    }

    public void removeSport(long id) {
        Sport sport = findSportById(id);

        if (sport.getPublishStatus().equals(PUBLISHED)) {
            if (sport.getUpdatedBy() == null) {
                removePublishedSport(sport);
                return;
            }
            removeUpdatedSport(sport);
            return;
        }

        if (!sport.getCategories().isEmpty()) {
            throw new ServiceException("Can't delete sport, because it contains categories!");
        }
        if (sport.getPhotoUrl() != null) {
            imageService.deleteImage(sport.getPhotoUrl());
        }
        userService.removeSportFromModerators(sport);
        sportRepository.deleteById(sport.getId());
    }

    private void removePublishedSport(Sport published) {
        if (!published.getCategories().isEmpty()) {
            throw new ServiceException("Can't delete sport, because it contains categories!");
        }

        if (published.getPhotoUrl() != null) {
            imageService.deleteImage(published.getPhotoUrl());
        }

        userService.removeSportsFromAllUsers(published);
        sportRepository.deleteById(published.getId());
    }

    @Transactional
    private void removeUpdatedSport(Sport sport) {
        Sport updated = sport.getUpdatedBy();
        sport.setUpdatedBy(null);
        sportRepository.save(sport);

        if (sport.getPhotoUrl() != null
            && updated.getPhotoUrl() != null
            && !updated.getPhotoUrl().equals(sport.getPhotoUrl())) {
            imageService.deleteImage(sport.getPhotoUrl());
        }

        userService.removeSportFromModerators(updated);
        sportRepository.deleteById(updated.getId());
    }

    public void removeMyListSport(long sportId, long userId) {
        Sport sport = sportRepository.findById(sportId).orElseThrow(() ->
                new ServiceException(String.format("Sport with id %d does not exist", sportId))
            );
        userService.removeSportFromUserList(sport, userId);
    }

    public void publish(Sport sport) {
        if (sport.getPublishStatus().equals(PUBLISHED)
            && sport.getUpdatedBy() != null) {
            publishUpdatedSport(sport);
        } else if (sport.getPublishStatus().equals(NOT_PUBLISHED)
                    || sport.getPublishStatus().equals(SCHEDULED)
        ) {
            publishCreatedSport(sport);
        } else if (sport.getPublishStatus().equals(DELETED)) {
            sportRepository.deleteById(sport.getId());
        }
    }

    private void publishUpdatedSport(Sport sport) {
        Sport updatedBy = sport.getUpdatedBy();

        String imageName = sport.getPhotoUrl();
        sport.setPhotoUrl(updatedBy.getPhotoUrl());
        sport.setName(updatedBy.getName());
        sport.setLastUpdated(LocalDate.now());
        sport.setPublishStatus(PUBLISHED);
        sport.setUpdatedBy(null);
        sport.setVariants(new ArrayList<>(updatedBy.getVariants()));

        sportRepository.save(sport);

        if (imageName != null
            && !imageName.equals(updatedBy.getPhotoUrl())) {
            imageService.deleteImage(imageName);
        }

        publishRemoveCopy(updatedBy.getId());
    }

    private void publishRemoveCopy(long copySportId) {
        Sport copy = findSportById(copySportId);
        sportRepository.deleteById(copy.getId());
    }

    private void publishCreatedSport(Sport sport) {
        sport.setLastUpdated(LocalDate.now());
        sport.setPublishStatus(PUBLISHED);
        sportRepository.save(sport);
    }

    public List<Sport> applyUpdatedFieldsToSports(List<Sport> sports) {
        return sports.stream()
            .map(this::applyUpdatedFieldsToSport)
            .toList();
    }

    private Sport applyUpdatedFieldsToSport(Sport sport) {
        if (!sport.getPublishStatus().equals(PUBLISHED)
            || sport.getUpdatedBy() == null) {
            return sport;
        }
        Sport updated = sport.getUpdatedBy();
        sport.setName(updated.getName());
        sport.setPhotoUrl(updated.getPhotoUrl());
        sport.setPublishStatus(updated.getPublishStatus());
        sport.setLastUpdated(updated.getLastUpdated());
        sport.setVariants(updated.getVariants());
        return sport;
    }
}
