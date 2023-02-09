package com.ktu.xsports.api.dto.response;

import com.ktu.xsports.api.domain.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
    long id;
    String name;
    String surname;
    String nickname;
    String email;
    String photoPath;
    Role role;
    boolean isBlocked;
}