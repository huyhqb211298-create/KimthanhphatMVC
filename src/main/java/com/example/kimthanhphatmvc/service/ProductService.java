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
    Page<Product> findFiltered(Long categoryId, Long brandId, Long productTypeId,String keyword, int page, int size);
    Optional<Product> findBySlug(String slug);
    List<Product> findRelatedProducts(Product product);

    Page<Product> findByCategoryPaged(Long categoryId, int page, int size);
    Page<Product> findByBrandPaged(Long brandId, int page, int size);
    Page<Product> findByProductTypePaged(Long typeId, int page, int size);

}
