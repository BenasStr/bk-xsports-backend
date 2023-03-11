package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.exceptions.ServiceException;
import com.ktu.xsports.api.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariantService {
    private final VariantRepository variantRepository;

    public Variant getMainVariant() {
        return variantRepository.findByName("Standard")
            .orElseThrow(() -> new ServiceException("Variant does not exist!"));
    }
}
