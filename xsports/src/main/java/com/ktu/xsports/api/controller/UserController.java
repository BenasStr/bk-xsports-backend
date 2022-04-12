package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.user.User;
import com.ktu.xsports.api.dto.request.user.UserRequest;
import com.ktu.xsports.api.dto.response.user.UserResponse;
import com.ktu.xsports.api.service.user.internal.UserCreatorImpl;
import com.ktu.xsports.api.service.user.internal.UserRetrieverImpl;
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

    private final UserRetrieverImpl userRetriever;
    private final UserCreatorImpl userCreator;
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

        return ResponseEntity.ok(
                PageableConverter.convert(page, size, userResponsePage)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable long id) {
        Optional<User> user = userRetriever.findById(id);

        return ResponseEntity.of(user.map(
                u -> Map.of("data", modelMapper.map(u, UserResponse.class))
        ));
    }

    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody @Valid UserRequest userRequest) {
        User user = userRequest.toUser();
        User newUser = userCreator.createUser(user);
        return ResponseEntity.ok("ToDo");
    }

    @PutMapping()
    public ResponseEntity<?> updateUser() {
        return ResponseEntity.ok("Todo");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        return ResponseEntity.ok(id);
    }
}
