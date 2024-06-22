package com.stores.stridestar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stores.stridestar.models.ProductAttribute;
import com.stores.stridestar.repositories.ProductAttributeRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;

    public List<ProductAttribute> getAllAttributes() {
        return productAttributeRepository.findAll();
    }

    public Optional<ProductAttribute> getAttributeById(Long id) {
        return productAttributeRepository.findById(id);
    }

    public ProductAttribute addAttribute(ProductAttribute productAttribute) {
        return productAttributeRepository.save(productAttribute);
    }

    public void updateAttribute(@NotNull ProductAttribute productAttribute) {
        ProductAttribute existingAttribute = productAttributeRepository.findById(productAttribute.getId())
                .orElseThrow(() -> new IllegalStateException("Attribute with ID " + productAttribute.getId() + " does not exist."));
        existingAttribute.setName(productAttribute.getName());
        productAttributeRepository.save(existingAttribute);
    }

    public void deleteAttributeById(Long id) {
        if (!productAttributeRepository.existsById(id)) {
            throw new IllegalStateException("Attribute with ID " + id + " does not exist.");
        }
        productAttributeRepository.deleteById(id);
    }
}