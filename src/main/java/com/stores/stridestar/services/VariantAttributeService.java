package com.stores.stridestar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stores.stridestar.models.VariantAttribute;
import com.stores.stridestar.repositories.VariantAttributeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantAttributeService {
    private final VariantAttributeRepository variantAttributeRepository;

    public List<VariantAttribute> getAllVariantAttributes() {
        return variantAttributeRepository.findAll();
    }

    public Optional<VariantAttribute> getVariantAttributeById(Long id) {
        return variantAttributeRepository.findById(id);
    }

    public VariantAttribute addVariantAttribute(VariantAttribute variantAttribute) {
        VariantAttribute item = null;
        try {
            item = variantAttributeRepository.save(variantAttribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public void deleteVariantAttributeById(Long id) {
        if (!variantAttributeRepository.existsById(id)) {
            throw new IllegalStateException("Variant Attribute with ID " + id + " does not exist.");
        }
        variantAttributeRepository.deleteById(id);
    }
}