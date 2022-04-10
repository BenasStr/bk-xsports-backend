package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.role.FindRole;
import com.ktu.xsports.api.domain.role.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>, FindRole {

}
