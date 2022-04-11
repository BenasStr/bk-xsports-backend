package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.user.User;
import com.ktu.xsports.api.dto.response.user.UserResponse;
import com.ktu.xsports.api.service.user.internal.UserRetrieverImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserRetrieverImpl userRetriever;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20", name = "per_page") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userRetriever.findUser(pageable);
        Page<UserResponse> userResponsePage = usersPage.map(
                user -> modelMapper.map(user, UserResponse.class)
        );
        return ResponseEntity.ok(PageableConverter.convert(page, size, userResponsePage));
    }
}
