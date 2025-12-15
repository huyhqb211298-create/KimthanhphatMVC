package com.example.kimthanhphatmvc.service.inventory.impl;

import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.model.enums.InventoryActionType;
import com.example.kimthanhphatmvc.model.inventory.*;
import com.example.kimthanhphatmvc.repository.inventory.*;
import com.example.kimthanhphatmvc.service.inventory.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryImportRepository inventoryImportRepository;
    private final InventoryExportRepository inventoryExportRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryStockRepository inventoryStockRepository;

    /* ================= NHẬP KHO ================= */
    @Override
    @Transactional
    public InventoryImport importStock(InventoryImport inventoryImport) {

        inventoryImport.setImportDate(LocalDateTime.now());

        // 🔥 GÁN CHA CHO ITEM (BẮT BUỘC)
        for (InventoryImportItem item : inventoryImport.getItems()) {
            item.setInventoryImport(inventoryImport);
        }

        InventoryImport savedImport =
                inventoryImportRepository.save(inventoryImport);

        for (InventoryImportItem item : savedImport.getItems()) {

            Product product = item.getProduct();

            InventoryStock stock =
                    inventoryStockRepository.findByProductId(product.getId())
                            .orElse(null);

            if (stock == null) {
                stock = new InventoryStock();
                stock.setProduct(product);
                stock.setQuantity(0);
            }

            int before = stock.getQuantity();
            int after = before + item.getQuantity();

            stock.setQuantity(after);
            inventoryStockRepository.save(stock);

            InventoryMovement movement = new InventoryMovement();
            movement.setActionDate(LocalDateTime.now());
            movement.setActionType(InventoryActionType.IMPORT);
            movement.setProduct(product);
            movement.setQuantityChange(item.getQuantity());
            movement.setStockBefore(before);
            movement.setStockAfter(after);
            movement.setReferenceId(savedImport.getId());

            inventoryMovementRepository.save(movement);
        }

        return savedImport;
    }


    /* ================= XUẤT KHO ================= */
    @Override
    @Transactional
    public InventoryExport exportStock(InventoryExport inventoryExport) {

        inventoryExport.setExportDate(LocalDateTime.now());

        // ⭐⭐ DÒNG QUYẾT ĐỊNH: SET QUAN HỆ NGƯỢC
        if (inventoryExport.getItems() != null) {
            for (InventoryExportItem item : inventoryExport.getItems()) {
                item.setInventoryExport(inventoryExport);
            }
        }

        InventoryExport savedExport =
                inventoryExportRepository.save(inventoryExport);

        for (InventoryExportItem item : savedExport.getItems()) {

            Product product = item.getProduct();

            InventoryStock stock = inventoryStockRepository
                    .findByProductId(product.getId())
                    .orElseThrow(() ->
                            new IllegalStateException("Sản phẩm chưa có tồn kho")
                    );

            int before = stock.getQuantity();
            int exportQty = item.getQuantity();

            if (exportQty > before) {
                throw new IllegalStateException("Không đủ tồn kho");
            }

            int after = before - exportQty;

            stock.setQuantity(after);
            inventoryStockRepository.save(stock);

            InventoryMovement movement = new InventoryMovement();
            movement.setActionDate(LocalDateTime.now());
            movement.setActionType(InventoryActionType.EXPORT);
            movement.setProduct(product);
            movement.setQuantityChange(-exportQty);
            movement.setStockBefore(before);
            movement.setStockAfter(after);
            movement.setReferenceId(savedExport.getId());

            inventoryMovementRepository.save(movement);
        }

        return savedExport;
    }

}
