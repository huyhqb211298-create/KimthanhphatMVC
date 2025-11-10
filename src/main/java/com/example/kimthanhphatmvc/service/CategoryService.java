package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category category);
    void deleteById(Long id);
    Optional<Category> findBySlug(String slug);
    boolean existsByName(String name);

}
