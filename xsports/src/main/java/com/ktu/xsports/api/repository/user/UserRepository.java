package com.ktu.xsports.api.repository.user;

import com.ktu.xsports.api.repository.user.internal.FindUser;
import com.ktu.xsports.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, FindUser {

}
