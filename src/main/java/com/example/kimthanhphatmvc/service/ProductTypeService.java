package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductTypeService {
    List<ProductType> findAll();
    Optional<ProductType> findById(Long id);
    ProductType save(ProductType productType);
    void deleteById(Long id);
    boolean existsByName(String name);
    Optional<ProductType> findBySlug(String slug);
}
