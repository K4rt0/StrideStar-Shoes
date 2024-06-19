package com.stores.stridestar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stores.stridestar.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}