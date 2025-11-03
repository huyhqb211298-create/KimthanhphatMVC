package com.example.kimthanhphatmvc.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Getters & Setters ---
    // (bạn có thể dùng Lombok sau này để rút gọn)
}
