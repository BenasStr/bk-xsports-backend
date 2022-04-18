package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.repository.RoleRepository;
import com.ktu.xsports.api.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping()
    public ResponseEntity<?> findRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(Map.of("data", roles));
    }
}
