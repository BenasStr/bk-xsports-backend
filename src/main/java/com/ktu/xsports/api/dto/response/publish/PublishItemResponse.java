package com.ktu.xsports.api.dto.response.publish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishItemResponse {
    long id;
    String name;
    String status;
}
