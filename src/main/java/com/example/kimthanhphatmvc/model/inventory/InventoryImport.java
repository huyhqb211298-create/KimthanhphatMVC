package com.example.kimthanhphatmvc.model.inventory;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory_imports")
@Data
public class InventoryImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime importDate;

    private String createdBy;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "inventoryImport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventoryImportItem> items;
}

