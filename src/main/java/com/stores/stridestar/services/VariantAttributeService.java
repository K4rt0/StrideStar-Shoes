package com.stores.stridestar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stores.stridestar.extensions.ResourceNotFoundException;
import com.stores.stridestar.models.VariantAttribute;
import com.stores.stridestar.repositories.ProductAttributeValueRepository;
import com.stores.stridestar.repositories.ProductVariantRepository;
import com.stores.stridestar.repositories.VariantAttributeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantAttributeService {
    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductAttributeValueRepository attributeValueRepository;

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

    public void updateVariantAttribute(VariantAttribute variantAttribute) {
        VariantAttribute existingVariantAttribute = variantAttributeRepository.findById(variantAttribute.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID does not exist."));
        existingVariantAttribute.setProductAttributeValue(attributeValueRepository.findById(variantAttribute.getProductAttributeValue().getId()).get());
        existingVariantAttribute.setProductVariant(productVariantRepository.findById(variantAttribute.getProductVariant().getId()).get());
        variantAttributeRepository.save(existingVariantAttribute);
    }

    public void deleteVariantAttributeById(Long id) {
        if (!variantAttributeRepository.existsById(id)) {
            throw new IllegalStateException("Variant Attribute with ID " + id + " does not exist.");
        }
        variantAttributeRepository.deleteById(id);
    }
}