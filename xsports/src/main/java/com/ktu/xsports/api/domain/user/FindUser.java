package com.ktu.xsports.api.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FindUser {

    Optional<User> findById(long id);

    Page<User> findAll(Pageable pageable);
}
