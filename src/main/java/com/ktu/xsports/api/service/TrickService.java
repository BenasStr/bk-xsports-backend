package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Trick;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.TrickRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.text.html.Option;

@Service
@RequiredArgsConstructor
public class TrickService {

    private final TrickRepository trickRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public List<Trick> findTricks(Long sportId, Long categoryId, String difficulty, String email) {
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new ServiceException("User doesn't exist"));

        Category category = categoryService.findCategory(sportId, categoryId);

        return difficulty.equals("all") ?
            trickRepository.findAll(user.getId(), categoryId) :
            trickRepository.findAll(user.getId(), categoryId, difficulty);
    }

    public Optional<Trick> findTrickById(Long sportId, Long categoryId, Long trickId, String email) {
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new ServiceException("User doesn't exist!"));

        Category category = categoryService.findCategory(sportId, categoryId);

        return trickRepository.findById(categoryId, trickId, user.getId());
    }

    @Transactional
    public Trick createTrick(Long sportId, Long categoryId, Trick trick) {
        Category category = categoryService.findCategory(sportId, categoryId);
        trick.setCategory(category);
        return trickRepository.save(trick);
    }

    public Optional<Trick> updateTrick(Long sportId, Long categoryId, Trick newTrick, Long trickId) {
        Category category = categoryService.findCategory(sportId, categoryId);
        Optional<Trick> existingTrick = trickRepository.findById(categoryId, trickId);
        if(existingTrick.isPresent()) {
            newTrick.setId(trickId);
            newTrick.setCategory(category);
            return Optional.of(trickRepository.save(newTrick));
        }
        return Optional.empty();
    }

    public Optional<Trick> removeTrick(Long sportId, Long categoryId, Long trickId) {
        categoryService.findCategory(sportId, categoryId);
        Optional<Trick> deletedTrick = trickRepository.findById(categoryId, trickId);
        if(deletedTrick.isPresent()) {
            trickRepository.delete(deletedTrick.get());
            return deletedTrick;
        }
        return Optional.empty();
    }
}
