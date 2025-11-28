package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByBrandId(Long brandId, Pageable pageable);

    Optional<Product> findBySlug(String slug);

    Page<Product> findByCategoryIdAndBrandId(Long categoryId, Long brandId, Pageable pageable);
    Page<Product> findByProductTypeId(Long productTypeId, Pageable pageable);
    Page<Product> findByCategoryIdAndProductTypeId(Long categoryId, Long productTypeId, Pageable pageable);
    Page<Product> findByBrandIdAndProductTypeId(Long brandId, Long productTypeId, Pageable pageable);
    Page<Product> findByCategoryIdAndBrandIdAndProductTypeId(Long categoryId, Long brandId, Long productTypeId, Pageable pageable);

    boolean existsBySlug(String slug);


    // === RELATED PRODUCTS ===
    List<Product> findTop4ByBrandIdAndIdNotOrderByIdDesc(Long brandId, Long excludeId);
    List<Product> findTop4ByProductTypeIdAndIdNotOrderByIdDesc(Long productTypeId, Long excludeId);
    List<Product> findTop4ByCategoryIdAndIdNotOrderByIdDesc(Long categoryId, Long excludeId);


    // =====================================================================
    // 🔥 SEARCH KHÔNG DẤU + FILTER category/brand/type
    // =====================================================================
    @Query("""
        SELECT p FROM Product p
        WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
          AND (:brandId IS NULL OR p.brand.id = :brandId)
          AND (:productTypeId IS NULL OR p.productType.id = :productTypeId)
          AND (:keyword IS NULL OR LOWER(p.nameNoAccent) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<Product> filter(@Param("categoryId") Long categoryId,
                         @Param("brandId") Long brandId,
                         @Param("productTypeId") Long productTypeId,
                         @Param("keyword") String keyword,
                         Pageable pageable);
}
