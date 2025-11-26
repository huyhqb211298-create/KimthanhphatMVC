package com.example.kimthanhphatmvc.model;

import jakarta.persistence.*;
import lombok.*;
import java.text.Normalizer;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_types")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "productType", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products;



}
