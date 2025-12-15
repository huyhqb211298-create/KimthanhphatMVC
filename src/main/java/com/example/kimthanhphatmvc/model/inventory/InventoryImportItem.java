package com.example.kimthanhphatmvc.model.inventory;

import com.example.kimthanhphatmvc.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_import_items")
@Data
public class InventoryImportItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "import_id")
    private InventoryImport inventoryImport;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Double importPrice;
}
