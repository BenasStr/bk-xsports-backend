package com.ktu.xsports.api.service.user.internal;

import com.ktu.xsports.api.domain.user.User;

import java.util.Optional;

public interface UserRemoverImpl {
    Optional<User> removeUser(long id);
}
