package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.ProductType;
import com.example.kimthanhphatmvc.repository.ProductTypeRepository;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public Optional<ProductType> findById(Long id) {
        return productTypeRepository.findById(id);
    }

    @Override
    public ProductType save(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    @Override
    public void deleteById(Long id) {
        productTypeRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productTypeRepository.existsByName(name);
    }

    @Override
    public Optional<ProductType> findBySlug(String slug) {
        return productTypeRepository.findBySlug(slug);
    }
}
