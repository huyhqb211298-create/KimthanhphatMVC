package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryExportItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryExportItemRepository extends JpaRepository<InventoryExportItem, Long> {

    List<InventoryExportItem> findByInventoryExportId(Long inventoryExportId);
}
