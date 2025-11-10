package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    Optional<ProductType> findBySlug(String slug);
    boolean existsByName(String name);
}
