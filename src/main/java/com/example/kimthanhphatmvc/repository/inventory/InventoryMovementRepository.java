package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.enums.InventoryActionType;
import com.example.kimthanhphatmvc.model.inventory.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    // ===== DASHBOARD =====
    long countByActionTypeAndActionDateBetween(
            InventoryActionType actionType,
            LocalDateTime from,
            LocalDateTime to
    );

    // ===== LỊCH SỬ KHO =====

    // theo sản phẩm
    List<InventoryMovement> findByProductIdOrderByActionDateDesc(Long productId);

    // theo khoảng thời gian
    List<InventoryMovement> findByActionDateBetweenOrderByActionDateDesc(
            LocalDateTime from,
            LocalDateTime to
    );

    // theo sản phẩm + thời gian
    List<InventoryMovement> findByProductIdAndActionDateBetweenOrderByActionDateDesc(
            Long productId,
            LocalDateTime from,
            LocalDateTime to
    );

    // ===== BÁO CÁO =====
    List<InventoryMovement> findByActionDateBetweenOrderByActionDateAsc(
            LocalDateTime from,
            LocalDateTime to
    );
}
