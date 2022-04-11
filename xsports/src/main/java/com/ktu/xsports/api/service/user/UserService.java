package com.ktu.xsports.api.service.user;

import com.ktu.xsports.api.domain.user.FindUser;
import com.ktu.xsports.api.domain.user.User;
import com.ktu.xsports.api.service.user.internal.UserRetrieverImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserRetrieverImpl {
    private final FindUser findUser;

    @Override
    public Page<User> findUser(Pageable pageable) {
        return findUser.findAll(pageable);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }
}
