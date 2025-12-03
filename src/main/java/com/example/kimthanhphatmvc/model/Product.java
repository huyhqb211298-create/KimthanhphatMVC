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

    @Column(name = "name_no_accent")
    private String nameNoAccent; // 🔥 Thêm trường không dấu

    private String slug;

    private Double price;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
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

    /* -----------------------------------------------------
        🔥 Override setter name để tự tạo tên không dấu
    ------------------------------------------------------ */
    public void setName(String name) {
        this.name = name;
        this.nameNoAccent = removeAccent(name);
    }

    /* -------------------------------------
        🔥 Hàm remove dấu tiếng Việt
    -------------------------------------- */
    private String removeAccent(String s) {
        if (s == null) return null;
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        String noAccent = normalized.replaceAll("\\p{M}", "");
        return noAccent.replace("đ", "d").replace("Đ", "D");
    }
}
