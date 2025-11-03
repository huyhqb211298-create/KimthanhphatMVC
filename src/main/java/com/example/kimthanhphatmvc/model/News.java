package com.example.kimthanhphatmvc.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Tiêu đề bài viết

    @Column(nullable = false, unique = true)
    private String slug; // Dùng cho URL SEO-friendly, vd: "huong-dan-pccc-nha-o"

    @Column(columnDefinition = "TEXT")
    private String summary; // Tóm tắt ngắn hiển thị ở trang danh sách

    @Column(columnDefinition = "LONGTEXT")
    private String content; // Nội dung chính

    private String thumbnail; // Ảnh đại diện bài viết

    private String author; // Tác giả (tùy chọn)

    private boolean published = true; // Trạng thái: đã xuất bản hay chưa

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();}

