package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    Optional<InventoryStock> findByProductId(Long productId);

    @Query("""
        SELECT s FROM InventoryStock s
        WHERE LOWER(s.product.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<InventoryStock> findByProductNameContainingIgnoreCase(@Param("keyword") String keyword);

    @Query("select sum(s.quantity) from InventoryStock s")
    Integer sumQuantity();

    List<InventoryStock> findTop5ByOrderByQuantityDesc();
    List<InventoryStock> findTop5ByOrderByQuantityAsc();

    List<InventoryStock> findByQuantityGreaterThan(Integer quantity);
}
