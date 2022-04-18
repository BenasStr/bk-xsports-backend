package com.ktu.xsports.api.service.user.internal;

import com.ktu.xsports.api.domain.User;

import java.util.Optional;

public interface UserRemover {
    Optional<User> removeUser(long id);
}
