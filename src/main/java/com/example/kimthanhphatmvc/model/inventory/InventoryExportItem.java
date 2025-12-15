package com.example.kimthanhphatmvc.model.inventory;

import com.example.kimthanhphatmvc.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_export_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryExportItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "export_id")
    private InventoryExport inventoryExport;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
}
