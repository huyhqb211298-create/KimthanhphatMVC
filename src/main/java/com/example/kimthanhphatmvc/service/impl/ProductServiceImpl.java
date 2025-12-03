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

    // ================= FILTER + PAGINATION ==================

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


    @Transactional(readOnly = true)
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

        // 2️⃣ Nếu chưa đủ 4 → bổ sung theo PRODUCT TYPE
        if (related.size() < 4 && product.getProductType() != null) {
            related.addAll(
                    productRepository.findTop4ByProductTypeIdAndIdNotOrderByIdDesc(
                            product.getProductType().getId(),
                            id
                    )
            );
        }

        // 3️⃣ Nếu vẫn chưa đủ 4 → bổ sung theo CATEGORY
        if (related.size() < 4 && product.getCategory() != null) {
            related.addAll(
                    productRepository.findTop4ByCategoryIdAndIdNotOrderByIdDesc(
                            product.getCategory().getId(),
                            id
                    )
            );
        }

        // 4️⃣ Trả về tối đa 4 sản phẩm, bỏ duplicate nếu trùng
        return related.stream()
                .distinct()
                .limit(4)
                .toList();
    }

}
