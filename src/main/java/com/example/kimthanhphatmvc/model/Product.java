package com.example.kimthanhphatmvc.model;

import com.example.kimthanhphatmvc.model.enums.PriceDisplay;
import com.example.kimthanhphatmvc.model.enums.StockStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.text.Normalizer;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slug;

    private Double price;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String image;

    private String origin;
    private Integer warrantyMonths;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @Enumerated(EnumType.STRING)
    private PriceDisplay priceDisplay;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonBackReference
    private Brand brand;

    @PrePersist
    @PreUpdate
    public void generateSlug() {
        if (this.slug == null || this.slug.isEmpty()) {
            if (this.slug == null || this.slug.isEmpty()) {
                String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);

                // Xóa dấu tiếng Việt + chuyển Đ/đ thành D/d
                normalized = normalized
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replace("Đ", "D")
                        .replace("đ", "d");

                this.slug = normalized
                        .toLowerCase()
                        .replaceAll("[^a-z0-9\\s-]", "")
                        .replaceAll("\\s+", "-")
                        .replaceAll("-+", "-");
            }
        }
    }}
