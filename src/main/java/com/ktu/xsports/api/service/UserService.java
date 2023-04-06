package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.ktu.xsports.api.util.Role.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> findUsers(Pageable pageable, String username) {
        if (username.equals("")) {
            log.info("Fetching all users");
            return userRepository.findAll(pageable);
        }
        log.info("Fetching all users by username");
        return userRepository.findByNicknameContaining(username, pageable);
    }

    public User findById(long id) {
        log.info("Finding user by id {}", id);
        return userRepository.findById(id)
            .orElseThrow(() -> new ServiceException(String.format("User by id: %d not found", id)));
    }

    public User findByEmail(String email) {
        log.info("Fetching user by email {}", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ServiceException(String.format("User by email: %s not found.", email)));
    }

    public User saveModeratorUser(User user) {
        log.info("Saving moderator user to database");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(MODERATOR);
        return userRepository.save(user);
    }

    public Optional<User> updateUserById(User user, long id) {
        log.info("Updating user by id");
        User existingUser = findById(id);
        user.setId(id);

        if (user.getPhotoUrl() == null) {
            user.setPhotoUrl(existingUser.getPhotoUrl());
        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        user.setRole(existingUser.getRole());
        user.setSports(existingUser.getSports());
        return Optional.of(userRepository.save(user));
    }

    public User removeUser(long id) {
        log.info("Removing user by id");
        User deletedUser = findById(id);
        userRepository.delete(deletedUser);
        return deletedUser;
    }
}
