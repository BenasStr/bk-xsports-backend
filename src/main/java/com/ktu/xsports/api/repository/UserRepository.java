package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mapping.model.SpELExpressionEvaluator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(""
        + "SELECT u FROM users u "
        + "WHERE u.nickname LIKE %:search% "
        + "OR u.name LIKE %:search% ")
    Page<User> findByUsernameContaining(String search, Pageable pageable);

    @Query(""
        + "SELECT u FROM users u "
        + "WHERE u.role = :role "
        + "AND (u.name LIKE %:search% OR u.surname LIKE %:search% OR u.nickname LIKE %:search%) ")
    Page<User> findByUsernameContainingAndRole(String search, String role, Pageable pageable);

    @Query(""
        + "SELECT u FROM users u "
        + "WHERE u.role = :role ")
    Page<User> findByRole(String role, Pageable pageable);

    @Query(""
        + "SELECT u FROM users u "
        + "WHERE u.role = 'ADMIN' "
        + "OR u.role = 'MODERATOR' ")
    List<User> findAllModerators();
}
