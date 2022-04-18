package com.ktu.xsports.api.repository.role.internal;

import com.ktu.xsports.api.domain.role.Role;

import java.util.List;
import java.util.Optional;

public interface FindRole {
    Optional<Role> findById(long id);
    List<Role> findAll();
}
