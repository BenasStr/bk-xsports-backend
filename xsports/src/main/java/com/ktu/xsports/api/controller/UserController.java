package com.ktu.xsports.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktu.xsports.api.converter.PageableConverter;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.UserRequest;
import com.ktu.xsports.api.dto.response.UserResponse;
import com.ktu.xsports.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
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

//    @GetMapping("token/refresh")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256("verySecretSecret".getBytes());
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(refresh_token);
//                String email = decodedJWT.getSubject();
//                Optional<User> user = userService.findByEmail(email);
//
//                String access_token = JWT.create()
//                        .withSubject(user.get().getUserName())
//                        .withExpiresAt(new Date(System.currentTimeMillis() + 600000))
//                        .withIssuer(request.getRequestURL().toString())
////                        .withClaim("role",
////                                user.get().)
//                        .sign(algorithm);
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refresh_token);
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//            } catch (Exception exception) {
//                log.error("Error logging in: {}", exception.getMessage());
//                response.setHeader("error", exception.getMessage());
//                response.setStatus(FORBIDDEN.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", exception.getMessage());
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//        }
//        throw new RuntimeException("Refresh token is missing");
//    }
}
