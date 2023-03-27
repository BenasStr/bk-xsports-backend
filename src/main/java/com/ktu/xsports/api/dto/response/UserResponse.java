package com.ktu.xsports.api.dto.response;

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
    String role;
    boolean isBlocked;
}