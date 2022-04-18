package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.UserRequest;
import com.ktu.xsports.api.dto.response.UserResponse;
import com.ktu.xsports.api.exceptions.RoleException;
import com.ktu.xsports.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> findUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20", name = "per_page") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userService.findUser(pageable);
        Page<UserResponse> userResponsePage = usersPage.map(
                user -> modelMapper.map(user, UserResponse.class)
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

    @PostMapping()
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserRequest userRequest)
            throws RoleException {
        User user = userRequest.toUser();
        Optional<User> newUser = userService.createUser(user);

        return ResponseEntity.of(
                newUser.map(u ->
                        Map.of("data", modelMapper.map(u, UserResponse.class))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserRequest userRequest,
            @PathVariable long id
    ) {
        User user = userRequest.toUser();
        Optional<User> updatedUser = userService.updateUser(user, id);

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
