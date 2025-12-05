package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.repository.ProductRepository;
import com.example.kimthanhphatmvc.service.ProductService;
import com.example.kimthanhphatmvc.service.SlugService;
import com.example.kimthanhphatmvc.util.TextUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SlugService slugService;

    public ProductServiceImpl(ProductRepository productRepository, SlugService slugService) {
        this.productRepository = productRepository;
        this.slugService = slugService;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Product product) {

        // ⭐ Chỉ tạo slug khi thêm mới (id == null)
        if (product.getId() == null || product.getSlug() == null || product.getSlug().isEmpty()) {
            String slug = slugService.createSlug(product.getName());
            product.setSlug(slug);
        }

        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    // ======================================================
    //  FILTER + PAGINATION (used by /products page)
    // ======================================================

    @Override
    public Page<Product> findFiltered(Long categoryId,
                                      Long brandId,
                                      Long productTypeId,
                                      String keyword,
                                      int page,
                                      int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        // 🔥 CHUẨN HÓA KEYWORD: xoá dấu + lowercase
        String normalizedKeyword = null;

        if (keyword != null && !keyword.isBlank()) {
            normalizedKeyword = TextUtils.removeAccent(keyword).toLowerCase();
        }

        return productRepository.filter(
                categoryId,
                brandId,
                productTypeId,
                normalizedKeyword,
                pageable
        );
    }

    // ======================================================
    //  SLUG-BASED PAGINATION (CATEGORY / BRAND / PRODUCT TYPE)
    // ======================================================

    @Override
    public Page<Product> findByCategoryPaged(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> findByBrandPaged(Long brandId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return productRepository.findByBrandId(brandId, pageable);
    }

    @Override
    public Page<Product> findByProductTypePaged(Long typeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return productRepository.findByProductTypeId(typeId, pageable);
    }


    // ======================================================
    //  RELATED PRODUCTS
    // ======================================================

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    @Override
    public List<Product> findRelatedProducts(Product product) {

        List<Product> related = new ArrayList<>();

        Long id = product.getId();

        // 1️⃣ Ưu tiên cùng BRAND
        if (product.getBrand() != null) {
            related.addAll(
                    productRepository.findTop4ByBrandIdAndIdNotOrderByIdDesc(
                            product.getBrand().getId(),
                            id
                    )
            );
        }

        // 2️⃣ Nếu chưa đủ 4 → TYPE
        if (related.size() < 4 && product.getProductType() != null) {
            related.addAll(
                    productRepository.findTop4ByProductTypeIdAndIdNotOrderByIdDesc(
                            product.getProductType().getId(),
                            id
                    )
            );
        }

        // 3️⃣ Nếu chưa đủ 4 → CATEGORY
        if (related.size() < 4 && product.getCategory() != null) {
            related.addAll(
                    productRepository.findTop4ByCategoryIdAndIdNotOrderByIdDesc(
                            product.getCategory().getId(),
                            id
                    )
            );
        }

        return related.stream()
                .distinct()
                .limit(4)
                .toList();
    }
}
