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
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products;

    @PrePersist
    @PreUpdate
    public void generateSlug() {
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
}}}