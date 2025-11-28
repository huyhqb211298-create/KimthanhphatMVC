package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.repository.*;
import com.example.kimthanhphatmvc.service.SlugService;
import com.example.kimthanhphatmvc.util.SlugUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlugServiceImpl implements SlugService {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductTypeRepository productTypeRepository;
    @Autowired private BrandRepository brandRepository;
    @Autowired private CategoryRepository categoryRepository;

    // THÊM NEWS
    @Autowired private NewsRepository newsRepository;

    @Override
    public boolean existsInAnyTable(String slug) {
        return productRepository.existsBySlug(slug)
                || productTypeRepository.existsBySlug(slug)
                || brandRepository.existsBySlug(slug)
                || categoryRepository.existsBySlug(slug)
                || newsRepository.existsBySlug(slug);   // NEW
    }

    @Override
    public String generateUniqueSlug(String baseSlug) {
        String slug = baseSlug;
        int count = 1;

        while (existsInAnyTable(slug)) {
            slug = baseSlug + "-" + count;
            count++;
        }

        return slug;
    }

    @Override
    public String createSlug(String name) {
        String baseSlug = SlugUtil.toSlug(name);
        return generateUniqueSlug(baseSlug);
    }
}
