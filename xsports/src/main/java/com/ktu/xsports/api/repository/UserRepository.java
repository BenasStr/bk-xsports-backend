package com.ktu.xsports.api.repository;

import com.ktu.xsports.api.domain.user.FindUser;
import com.ktu.xsports.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, FindUser {

}
