package com.stores.stridestar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stores.stridestar.models.VariantAttribute;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, Long> {}