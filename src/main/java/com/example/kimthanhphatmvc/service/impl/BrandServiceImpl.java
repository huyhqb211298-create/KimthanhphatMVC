package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.repository.BrandRepository;
import com.example.kimthanhphatmvc.repository.CategoryRepository;
import com.example.kimthanhphatmvc.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.kimthanhphatmvc.model.Brand;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
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
        return brandRepository.save(brand);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public Optional<Brand> findBySlug(String slug) {
        return Optional.empty();
    }
}
