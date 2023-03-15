package com.ktu.xsports.api.dto.request;

import com.ktu.xsports.api.domain.Variant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantRequest {

    @NotNull
    private String name;

    public Variant toVariant() {
        return Variant.builder()
            .name(name)
            .build();
    }
}
