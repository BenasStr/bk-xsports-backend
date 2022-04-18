package com.ktu.xsports.api.service.user;

import com.ktu.xsports.api.domain.Role;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.exceptions.RoleException;
import com.ktu.xsports.api.repository.RoleRepository;
import com.ktu.xsports.api.repository.UserRepository;
import com.ktu.xsports.api.service.user.internal.UserCreator;
import com.ktu.xsports.api.service.user.internal.UserRemover;
import com.ktu.xsports.api.service.user.internal.UserRetriever;
import com.ktu.xsports.api.service.user.internal.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserRetriever, UserCreator, UserUpdater, UserRemover {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Page<User> findUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> createUser(User user) throws RoleException {
        Role role = roleRepository.findById(2)
                .orElseThrow(RoleException::new);

        user.setRole(role);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> updateUser(User user, long id) {
        user.setId(id);
        if (userRepository.findById(id).isPresent()) {
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> removeUser(long id) {
        Optional<User> deletedUser = userRepository.findById(id);
        if(deletedUser.isPresent()) {
            userRepository.delete(deletedUser.get());
            return deletedUser;
        }
        return Optional.empty();
    }
}
