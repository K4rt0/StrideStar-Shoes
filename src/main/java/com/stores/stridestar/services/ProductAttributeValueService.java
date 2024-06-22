package com.stores.stridestar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stores.stridestar.models.ProductAttributeValue;
import com.stores.stridestar.repositories.ProductAttributeValueRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAttributeValueService {
    private final ProductAttributeValueRepository productAttributeValueRepository;

    public List<ProductAttributeValue> getAllAttributeValue() {
        return productAttributeValueRepository.findAll();
    }

    public List<ProductAttributeValue> getAllByProductAttributeId(Long attributeId) {
        return productAttributeValueRepository.findByProductAttributeId(attributeId);
    }

    public Optional<ProductAttributeValue> getAttributeValueById(Long id) {
        return productAttributeValueRepository.findById(id);
    }

    public void addAttributeValue(ProductAttributeValue productAttributeValue) {
        productAttributeValueRepository.save(productAttributeValue);
    }

    public void updateAttributeValue(@NotNull ProductAttributeValue productAttributeValue) {
        ProductAttributeValue existingAttributeValue = productAttributeValueRepository.findById(productAttributeValue.getId())
                .orElseThrow(() -> new IllegalStateException("Attribute Value with ID " + productAttributeValue.getId() + " does not exist."));
        existingAttributeValue.setValue(existingAttributeValue.getValue());
        productAttributeValueRepository.save(existingAttributeValue);
    }

    public void deleteAttributeValueById(Long id) {
        if (!productAttributeValueRepository.existsById(id)) {
            throw new IllegalStateException("Attribute Value with ID " + id + " does not exist.");
        }
        productAttributeValueRepository.deleteById(id);
    }
}