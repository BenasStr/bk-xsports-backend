package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.UserRequest;
import com.ktu.xsports.api.dto.response.UserBasicResponse;
import com.ktu.xsports.api.dto.response.UserResponse;
import com.ktu.xsports.api.service.JwtService;
import com.ktu.xsports.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final static int HEADER_START_LENGTH = 7;

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<?> findUserMe(@RequestHeader("Authorization") String authorization) {
        String email = jwtService.extractUsername(authorization.substring(HEADER_START_LENGTH));
        Optional<User> user = userService.findByEmail(email);
        return ResponseEntity.of(user.map(
            u -> Map.of("data", modelMapper.map(u, UserBasicResponse.class))
        ));
    }

    @GetMapping()
    public ResponseEntity<?> findUsers (
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20", name = "per_page") int size,
        @RequestParam(defaultValue = "", name = "username") String username
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userService.findUsers(pageable, username);
        Page<UserResponse> userResponsePage = usersPage.map(
                user -> modelMapper.map(user, UserResponse.class)
        );

        return ResponseEntity.ok(
                PageableConverter.convert(page, size, userResponsePage)
        );
    }

    @GetMapping("/basic")
    public ResponseEntity<?> findUsersBasic (
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20", name = "per_page") int size,
        @RequestParam(defaultValue = "", name = "username") String username
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userService.findUsers(pageable, username);
        Page<UserBasicResponse> userResponsePage = usersPage.map(
            user -> modelMapper.map(user, UserBasicResponse.class)
        );

        return ResponseEntity.ok(
            PageableConverter.convert(page, size, userResponsePage)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable long id) {
        Optional<User> user = userService.findById(id);

        return ResponseEntity.of(user.map(
                u -> Map.of("data", modelMapper.map(u, UserResponse.class))
        ));
    }

    @GetMapping("basic/{id}")
    public ResponseEntity<?> findUserBasic(@PathVariable long id) {
        Optional<User> user = userService.findById(id);

        return ResponseEntity.of(user.map(
            u -> Map.of("data", modelMapper.map(u, UserBasicResponse.class))
        ));
    }

    @PostMapping()
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserRequest userRequest)
    {
        User user = userRequest.toUser();
        User newUser = userService.saveUser(user);

        return ResponseEntity.ok(
                        Map.of("data", modelMapper.map(newUser, UserResponse.class))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserRequest userRequest,
            @PathVariable long id
    ) {
        User user = userRequest.toUser();
        Optional<User> updatedUser = userService.updateUserById(user, id);

        return ResponseEntity.of(
                updatedUser.map(u ->
                        Map.of("data", modelMapper.map(u, UserResponse.class))));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUserMe(
        @RequestBody @Valid UserRequest userRequest,
        @RequestHeader("Authorization") String authorization
    ) {
        String email = jwtService.extractUsername(authorization.substring(HEADER_START_LENGTH));
        User user = userRequest.toUser();
        Optional<User> updatedUser = userService.updateUserByEmail(user, email);

        return ResponseEntity.of(
            updatedUser.map(u ->
                Map.of("data", modelMapper.map(u, UserResponse.class))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        Optional<User> deletedUser = userService.removeUser(id);

        return ResponseEntity.of(
                deletedUser.map(u ->
                        Map.of("data", modelMapper.map(u, UserResponse.class))));
    }
}
