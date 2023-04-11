package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Variant;
import com.ktu.xsports.api.advice.exceptions.AlreadyExistsException;
import com.ktu.xsports.api.advice.exceptions.ServiceException;
import com.ktu.xsports.api.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantService {
    private final VariantRepository variantRepository;

    public Variant getStandardVariant() {
        return variantRepository.findByName("Standard")
            .orElseThrow(() -> new ServiceException("Variant does not exist!"));
    }

    public List<Variant> getVariants() {
        return variantRepository.findAll();
    }

    public Variant createVariant(Variant variant) {
        try {
            return variantRepository.save(variant);
        } catch (Exception e) {
            throw new AlreadyExistsException("Variant already exists");
        }
    }

    public Variant updateVariant(Variant variant, Long variantId) {
        try {
            Variant existing = variantRepository.findById(variantId)
                .orElseThrow(() -> new ServiceException("Variant doesn't exist"));

            variant.setId(existing.getId());
            variant.setSports(existing.getSports());
            variant.setTrickVariants(existing.getTrickVariants());
            return variantRepository.save(variant);
        } catch (Exception e) {
            throw new AlreadyExistsException("Variant with that name already exists");
        }
    }

    public void deleteVariant(Long variantId) {
        Variant existing = variantRepository.findById(variantId)
            .orElseThrow(() -> new ServiceException("Variant doesn't exist"));

        if (existing.getTrickVariants().size() > 0 || existing.getSports().size() > 0 ) {
            throw new ServiceException("Variant is used");
        }
        variantRepository.deleteById(existing.getId());
    }
}
