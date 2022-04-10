package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.role.FindRole;
import com.ktu.xsports.api.domain.role.Role;
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

    private final FindRole findRole;

    @GetMapping()
    public ResponseEntity<?> findRoles() {
        List<Role> roles = findRole.findAll();
        return ResponseEntity.ok(Map.of("data", roles));
    }
}
