package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.Brand;
import com.example.kimthanhphatmvc.model.Category;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> findAll();
    Brand findById(Long id);
    Brand save(Brand brand);
    void deleteById(Long id);
    Optional<Brand> findBySlug(String slug);
    boolean existsByName(String name);

}