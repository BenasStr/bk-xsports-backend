package com.ktu.xsports.api.domain.role;

import java.util.List;
import java.util.Optional;

public interface FindRole {
    Optional<Role> findById(long id);
    List<Role> findAll();
}
