package com.ktu.xsports.api.repository.role;

import com.ktu.xsports.api.repository.role.internal.FindRole;
import com.ktu.xsports.api.domain.role.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>, FindRole {

}
