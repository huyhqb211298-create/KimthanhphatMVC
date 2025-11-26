package com.example.kimthanhphatmvc.model;

import com.example.kimthanhphatmvc.model.enums.PriceDisplay;
import com.example.kimthanhphatmvc.model.enums.StockStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.Data;
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

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    @JsonBackReference
    private ProductType productType;




}
