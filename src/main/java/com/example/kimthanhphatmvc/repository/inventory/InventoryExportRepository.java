package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryExportRepository extends JpaRepository<InventoryExport, Long> {
}
