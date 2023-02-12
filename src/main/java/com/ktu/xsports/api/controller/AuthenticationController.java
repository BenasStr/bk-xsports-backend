package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.dto.request.LoginRequest;
import com.ktu.xsports.api.dto.request.RegisterRequest;
import com.ktu.xsports.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        log.info("registering new user");
        return ResponseEntity.ok(Map.of("data", authenticationService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws Exception {
        log.info("user is trying to log in");
        return ResponseEntity.ok(Map.of("data", authenticationService.authenticate(request)));
    }
}