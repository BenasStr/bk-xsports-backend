package com.ktu.xsports.api.service.user;

import com.ktu.xsports.api.domain.Role;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.RoleRepository;
import com.ktu.xsports.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<User> findUser(Pageable pageable) {
        log.info("Fetching all users");
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(long id) {
        log.info("Finding user by id {}", id);
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByName(String name) {
        log.info("Fetching user by name {}", name);
        return userRepository.findByUserName(name);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving user to database");
        Role role = roleRepository.findByName("user").get();
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> updateUser(User user, long id) {
        log.info("Updating user by id");
        user.setId(id);
        if (userRepository.findById(id).isPresent()) {
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> removeUser(long id) {
        log.info("Removing user by id");
        Optional<User> deletedUser = userRepository.findById(id);
        if(deletedUser.isPresent()) {
            userRepository.delete(deletedUser.get());
            return deletedUser;
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);

        if(user.isPresent()) {
            log.info("User found in the database: {}", user.get().getUserName());
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(
                    new SimpleGrantedAuthority(user.get().getRole().getName())
            );

            return new org.springframework.security.core.userdetails.User(
                    user.get().getUserName(),
                    user.get().getPassword(),
                    authorities
            );
        }

        log.error("User not found in te database");
        throw new UsernameNotFoundException("User not found in te database");
    }
}
