package com.stores.stridestar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stores.stridestar.models.ProductVariant;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {}