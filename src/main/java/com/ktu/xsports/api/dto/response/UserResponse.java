package com.ktu.xsports.api.dto.response;

import com.ktu.xsports.api.domain.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserResponse {
    long id;
    String name;
    String surname;
    String userName;
    String email;
    String password;
    String photoPath;
    List<Role> roles;
}