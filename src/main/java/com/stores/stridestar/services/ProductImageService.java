package com.stores.stridestar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stores.stridestar.models.ProductImage;
import com.stores.stridestar.repositories.ProductImageRepository;

import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public List<ProductImage> getAllProductImages() {
        return productImageRepository.findAll();
    }

    public Optional<ProductImage> getProductImagesById(Long id) {
        return productImageRepository.findById(id);
    }

    public void addProductImage(ProductImage productImage) {
        productImageRepository.save(productImage);
    }

    public void updateProductImage(@NotNull ProductImage productImage) {
        ProductImage existingProduct = productImageRepository.findById(productImage.getId())
                .orElseThrow(
                        () -> new IllegalStateException("Product with ID " + productImage.getId() + " does not exist."));
        existingProduct.setUrl(productImage.getUrl());
        existingProduct.setProduct(productImage.getProduct());
        productImageRepository.save(existingProduct);
    }

    @Transactional
    public void removeAllProductImagesByProductId(Long productId) {
        productImageRepository.deleteByProductId(productId);
    }

    public void deleteProductImageById(Long id) {
        if (!productImageRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productImageRepository.deleteById(id);
    }
}