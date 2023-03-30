package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.dto.request.LoginRequest;
import com.ktu.xsports.api.dto.request.RegisterRequest;
import com.ktu.xsports.api.dto.response.AuthenticationResponse;
import com.ktu.xsports.api.service.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("Registering new user");
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok(Map.of("data", response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws Exception {
        log.info("User is trying to log in");
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(Map.of("data", response));
    }
}