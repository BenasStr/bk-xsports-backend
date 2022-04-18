package com.ktu.xsports.api.service.user.internal;

import com.ktu.xsports.api.domain.user.User;
import com.ktu.xsports.api.exceptions.RoleException;

import java.util.Optional;

public interface UserCreator {
    Optional<User> createUser(User user) throws RoleException;
}
