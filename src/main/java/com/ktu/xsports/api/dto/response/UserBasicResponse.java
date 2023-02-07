package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserBasicResponse {
    long id;
    String name;
    String surname;
    String userName;
    String photoPath;
    boolean isBlocked;
}