package com.example.kimthanhphatmvc.service.inventory;

import com.example.kimthanhphatmvc.model.inventory.InventoryImport;
import com.example.kimthanhphatmvc.model.inventory.InventoryExport;
public interface InventoryService {
    InventoryImport importStock(InventoryImport inventoryImport);
    InventoryExport exportStock(InventoryExport inventoryExport);
}
