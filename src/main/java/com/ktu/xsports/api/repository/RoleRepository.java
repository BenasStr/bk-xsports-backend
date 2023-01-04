package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(long id);

    Optional<Role> findByName(String name);
}
