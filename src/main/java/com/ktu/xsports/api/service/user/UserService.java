package com.ktu.xsports.api.service.user;

import com.ktu.xsports.api.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Optional<User> removeUser(long id);

    Page<User> findUser(Pageable pageable);

    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    Optional<User> updateUser(User user, long id);
    User saveUser(User user);
}
