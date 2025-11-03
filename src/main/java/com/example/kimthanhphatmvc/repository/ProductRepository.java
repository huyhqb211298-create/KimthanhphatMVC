package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByBrandId(Long brandId, Pageable pageable);
    Optional<Product> findBySlug(String slug);
    Page<Product> findByCategoryIdAndBrandId(Long categoryId, Long brandId, Pageable pageable);
}
