package com.example.kimthanhphatmvc.model.inventory;

import com.example.kimthanhphatmvc.model.enums.InventoryExportReason;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventory_exports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryExport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime exportDate;

    private String createdBy;

    @Enumerated(EnumType.STRING)
    private InventoryExportReason reason;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "inventoryExport", cascade = CascadeType.ALL)
    private List<InventoryExportItem> items;
}
