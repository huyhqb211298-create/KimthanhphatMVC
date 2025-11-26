package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.Category;
import com.example.kimthanhphatmvc.repository.CategoryRepository;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.SlugService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SlugService slugService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, SlugService slugService) {
        this.categoryRepository = categoryRepository;
        this.slugService = slugService;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {

        // ⭐ CHỈ TẠO SLUG KHI CATEGORY MỚI
        if (category.getId() == null || category.getSlug() == null || category.getSlug().isEmpty()) {
            String slug = slugService.createSlug(category.getName());
            category.setSlug(slug);
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
