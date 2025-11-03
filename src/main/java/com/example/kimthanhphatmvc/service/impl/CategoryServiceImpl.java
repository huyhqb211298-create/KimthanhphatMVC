package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.Category;
import com.example.kimthanhphatmvc.repository.CategoryRepository;
import com.example.kimthanhphatmvc.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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

}
