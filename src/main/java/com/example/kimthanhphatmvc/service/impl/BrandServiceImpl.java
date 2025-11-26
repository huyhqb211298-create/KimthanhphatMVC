package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.Brand;
import com.example.kimthanhphatmvc.repository.BrandRepository;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.SlugService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final SlugService slugService;

    public BrandServiceImpl(BrandRepository brandRepository, SlugService slugService) {
        this.brandRepository = brandRepository;
        this.slugService = slugService;
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public Brand save(Brand brand) {

        // ⭐ CHỈ TẠO SLUG KHI BRAND MỚI
        if (brand.getId() == null || brand.getSlug() == null || brand.getSlug().isEmpty()) {
            String slug = slugService.createSlug(brand.getName());
            brand.setSlug(slug);
        }

        return brandRepository.save(brand);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public Optional<Brand> findBySlug(String slug) {
        return brandRepository.findBySlug(slug);
    }

    @Override
    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }
}
