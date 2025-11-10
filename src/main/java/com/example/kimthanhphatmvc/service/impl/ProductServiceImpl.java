package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.repository.ProductRepository;
import com.example.kimthanhphatmvc.service.ProductService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    // ==================== FILTER + PAGINATION ====================
    @Override
    public Page<Product> findFiltered(Long categoryId, Long brandId, Long productTypeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        if (categoryId != null && brandId != null && productTypeId != null) {
            return productRepository.findByCategoryIdAndBrandIdAndProductTypeId(categoryId, brandId, productTypeId, pageable);
        } else if (categoryId != null && brandId != null) {
            return productRepository.findByCategoryIdAndBrandId(categoryId, brandId, pageable);
        } else if (categoryId != null && productTypeId != null) {
            return productRepository.findByCategoryIdAndProductTypeId(categoryId, productTypeId, pageable);
        } else if (brandId != null && productTypeId != null) {
            return productRepository.findByBrandIdAndProductTypeId(brandId, productTypeId, pageable);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable);
        } else if (brandId != null) {
            return productRepository.findByBrandId(brandId, pageable);
        } else if (productTypeId != null) {
            return productRepository.findByProductTypeId(productTypeId, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    @Override
    public Optional<Product> findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    @Override
    public List<Product> findRelated(Long categoryId, Long excludeProductId) {
        return productRepository.findByCategoryId(categoryId, Pageable.ofSize(4))
                .stream()
                .filter(p -> !p.getId().equals(excludeProductId))
                .toList();
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId, Pageable.unpaged()).getContent();
    }

    @Override
    public List<Product> findByBrand(Long brandId) {
        return productRepository.findByBrandId(brandId, Pageable.unpaged()).getContent();
    }

    @Override
    public List<Product> findByCategoryAndBrand(Long categoryId, Long brandId) {
        return productRepository.findByCategoryIdAndBrandId(categoryId, brandId, Pageable.unpaged()).getContent();
    }
}
