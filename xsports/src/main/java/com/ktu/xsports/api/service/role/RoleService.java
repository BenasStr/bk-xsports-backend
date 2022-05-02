package com.ktu.xsports.api.service.role;

import com.ktu.xsports.api.domain.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findRoleById(long id);

    Optional<Role> findRoleByName(String name);
    List<Role> findAllRoles();
}
