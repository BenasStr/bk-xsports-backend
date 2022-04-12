package com.ktu.xsports.api.dto.request.user;

import com.ktu.xsports.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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
    @Email
    private String email;

    @NotNull
    private String password;

    private String photoPath;

    public User toUser() {
        return User.builder()
                .name(name)
                .surname(surname)
                .userName(userName)
                .email(email)
                .password(password)
                .photoPath(photoPath)
                .build();
    }
}
