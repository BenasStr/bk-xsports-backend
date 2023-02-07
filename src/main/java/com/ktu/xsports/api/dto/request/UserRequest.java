package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String userName;

    @NotNull
    private String password;

    private String photoPath;

    public User toUser() {
        return User.builder()
                .name(name)
                .surname(surname)
                .userName(userName)
                .password(password)
                .photoPath(photoPath)
                .build();
    }
}
