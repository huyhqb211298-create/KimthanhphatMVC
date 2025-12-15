package com.example.kimthanhphatmvc.repository.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryImport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@Repository
public interface InventoryImportRepository
        extends JpaRepository<InventoryImport, Long> {
}


