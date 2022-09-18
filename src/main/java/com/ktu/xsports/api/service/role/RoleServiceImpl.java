package com.ktu.xsports.api.service.role;

import com.ktu.xsports.api.domain.Role;
import com.ktu.xsports.api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findRoleById(long id) {
        log.info("Fetching role by id {}", id);
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> findRoleByName(String name) {
        log.info("Fetching role by name {}", name);
        return roleRepository.findByName(name);
    }

    @Override
    public List<Role> findAllRoles() {
        log.info("Fetching all data");
        return roleRepository.findAll();
    }
}
