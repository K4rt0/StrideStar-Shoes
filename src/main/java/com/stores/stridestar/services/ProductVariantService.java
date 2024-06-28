package com.stores.stridestar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stores.stridestar.models.ProductVariant;
import com.stores.stridestar.repositories.ProductVariantRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;

    public List<ProductVariant> getAllProductVariants() {
        return productVariantRepository.findAll();
    }

    public Optional<ProductVariant> getProductVariantsById(Long id) {
        return productVariantRepository.findById(id);
    }

    public ProductVariant addProductVariant(ProductVariant productVariant) {
        return productVariantRepository.save(productVariant);
    }

    public void updateProductVariant(@NotNull ProductVariant productVariant) {
        ProductVariant existingProductVariant = productVariantRepository.findById(productVariant.getId())
                .orElseThrow(
                        () -> new IllegalStateException("Product with ID " + productVariant.getId() + " does not exist."));

        existingProductVariant.setPrice(productVariant.getPrice());
        existingProductVariant.setQuantity(productVariant.getQuantity());
        existingProductVariant.setProduct(productVariant.getProduct());
        productVariantRepository.save(existingProductVariant);
    }

    public void deleteById(Long id) {
        if (!productVariantRepository.existsById(id)) {
            throw new IllegalStateException("Product Variant with ID " + id + " does not exist.");
        }
        productVariantRepository.deleteById(id);
    }
}
