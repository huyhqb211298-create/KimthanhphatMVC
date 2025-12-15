package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryExportItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryExportItemRepository
        extends JpaRepository<InventoryExportItem, Long> {

    List<InventoryExportItem> findByInventoryExportId(Long inventoryExportId);

}
