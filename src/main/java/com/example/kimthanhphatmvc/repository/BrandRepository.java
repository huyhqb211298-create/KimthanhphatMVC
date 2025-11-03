package com.example.kimthanhphatmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.kimthanhphatmvc.model.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBySlug(String slug);

}
