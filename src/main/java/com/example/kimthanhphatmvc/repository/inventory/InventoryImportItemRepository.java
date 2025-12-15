package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryImportItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryImportItemRepository
        extends JpaRepository<InventoryImportItem, Long> {

    List<InventoryImportItem> findByInventoryImportId(Long importId);
}
