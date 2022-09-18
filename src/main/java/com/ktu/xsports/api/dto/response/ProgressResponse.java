package com.ktu.xsports.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProgressResponse {
    long id;
    long statusId;
    long trickId;
    long userId;
}
