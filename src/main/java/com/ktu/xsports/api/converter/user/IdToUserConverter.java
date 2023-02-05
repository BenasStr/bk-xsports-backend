package com.ktu.xsports.api.converter.user;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdToUserConverter extends StdConverter<Long, User> {

    private final UserRepository userRepository;

    @Override
    public User convert(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
