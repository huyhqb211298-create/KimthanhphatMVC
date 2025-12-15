package com.example.kimthanhphatmvc.model.inventory;

import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.model.enums.InventoryActionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime actionDate;

    @Enumerated(EnumType.STRING)
    private InventoryActionType actionType;

    private Integer quantityChange;

    private Integer stockBefore;
    private Integer stockAfter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private Long referenceId;
}
