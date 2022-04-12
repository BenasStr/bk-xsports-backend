package com.ktu.xsports.api.domain.user;

import com.ktu.xsports.api.domain.role.Role;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String userName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String photoPath;

    @ManyToOne
    private Role role;
}
