package com.ktu.xsports.api.service.security;

import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.request.LoginRequest;
import com.ktu.xsports.api.dto.request.RegisterRequest;
import com.ktu.xsports.api.dto.response.AuthenticationResponse;
import com.ktu.xsports.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    AuthenticationService authenticationService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationManager authenticationManager;

    @Test
    void register() {
        AuthenticationResponse expected = new AuthenticationResponse("");

        Mockito.when(passwordEncoder.encode(any()))
            .thenReturn("");

        Mockito.when(jwtService.generateToken(any()))
            .thenReturn("");

        Mockito.when(userRepository.save(any()))
            .thenReturn(null);


        AuthenticationResponse actual = authenticationService.register(request());
        assertEquals(expected.getToken(), actual.getToken());
    }

    @Test
    void authenticate() throws Exception {
        AuthenticationResponse expected = new AuthenticationResponse("");

        Mockito.when(authenticationManager.authenticate(any()))
            .thenReturn(null);

        Mockito.when(userRepository.findByEmail(any()))
            .thenReturn(Optional.of(User.builder().build()));

        Mockito.when(jwtService.generateToken(any()))
            .thenReturn("");

        AuthenticationResponse actual = authenticationService.authenticate(loginRequest());
        assertEquals(expected.getToken(), actual.getToken());
    }

    private RegisterRequest request() {
        return RegisterRequest.builder()
            .email("test")
            .firstName("test")
            .lastName("test")
            .password("test")
            .nickname("test")
            .build();
    }

    private LoginRequest loginRequest() {
        return LoginRequest.builder()
            .email("test@gmail.com")
            .password("pass")
            .build();
    }
}