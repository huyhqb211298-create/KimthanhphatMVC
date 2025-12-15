package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    // ===== TỒN KHO =====
    Optional<InventoryStock> findByProductId(Long productId);

    List<InventoryStock> findByQuantityGreaterThan(int quantity);

    List<InventoryStock> findTop5ByOrderByQuantityDesc();

    List<InventoryStock> findTop5ByOrderByQuantityAsc();

    // tìm theo tên sản phẩm
    @Query("""
        SELECT s FROM InventoryStock s
        WHERE LOWER(s.product.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<InventoryStock> findByProductNameContainingIgnoreCase(String keyword);

    // tổng tồn kho
    @Query("SELECT SUM(s.quantity) FROM InventoryStock s")
    Integer sumQuantity();
}
