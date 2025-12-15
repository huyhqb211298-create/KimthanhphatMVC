package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.enums.InventoryActionType;
import com.example.kimthanhphatmvc.model.inventory.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    // Xem lịch sử biến động theo sản phẩm (mới nhất trước)
    List<InventoryMovement> findByProductIdOrderByActionDateDesc(Long productId);
    List<InventoryMovement> findByActionDateBetween(
            LocalDateTime from,
            LocalDateTime to
    );
    long countByActionTypeAndActionDateBetween(
            InventoryActionType actionType,
            LocalDateTime from,
            LocalDateTime to
    );

    @Query("""
    SELECT m FROM InventoryMovement m
    WHERE (:productId IS NULL OR m.product.id = :productId)
      AND (:fromDate IS NULL OR m.actionDate >= :fromDate)
      AND (:toDate IS NULL OR m.actionDate <= :toDate)
    ORDER BY m.actionDate DESC
""")
    List<InventoryMovement> filterMovements(
            @Param("productId") Long productId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    List<InventoryMovement> findByActionDateBetweenOrderByActionDateAsc(
            LocalDateTime from,
            LocalDateTime to
    );


}
