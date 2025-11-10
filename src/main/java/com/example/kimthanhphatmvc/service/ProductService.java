package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    void save(Product product);
    void deleteById(Long id);
    Page<Product> findFiltered(Long categoryId, Long brandId, Long productTypeId, int page, int size);
    Optional<Product> findBySlug(String slug);

    List<Product> findRelated(Long categoryId, Long excludeProductId);
    List<Product> findByCategory(Long categoryId);
    List<Product> findByBrand(Long brandId);
    List<Product> findByCategoryAndBrand(Long categoryId, Long brandId);
}
