package com.ktu.xsports.api.service.user.internal;

import com.ktu.xsports.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRetriever {

    Page<User> findUser(Pageable pageable);

    Optional<User> findById(long id);
}
