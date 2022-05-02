package com.ktu.xsports.api.controller;

import com.ktu.xsports.api.domain.Role;
import com.ktu.xsports.api.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<?> findRoles() {
        List<Role> roles = roleService.findAllRoles();
        return ResponseEntity.ok(Map.of("data", roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findRoleById (@PathVariable long id) {
        Optional<Role> role = roleService.findRoleById(id);
        return ResponseEntity.of(role.map(e ->
                Map.of("data", e)));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findRoleByName(@PathVariable String name) {
        Optional<Role> role = roleService.findRoleByName(name);
        return ResponseEntity.of(role.map(e ->
                Map.of("data", e)));
    }
}
