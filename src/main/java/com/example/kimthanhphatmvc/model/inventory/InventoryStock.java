package com.example.kimthanhphatmvc.model.inventory;

import com.example.kimthanhphatmvc.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "inventory_stocks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_inventory_stock_product", columnNames = "product_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mỗi Product chỉ có 1 dòng tồn kho hiện tại
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Tồn hiện tại (độc lập với StockStatus)
     */
    @Column(nullable = false)
    private Integer quantity = 0;
}
