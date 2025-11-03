package com.example.kimthanhphatmvc.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Tên người gửi

    @Column(nullable = false)
    private String email; // Email liên hệ

    private String phone; // Số điện thoại (nếu có)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message; // Nội dung liên hệ

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Thời gian gửi

    private boolean replied = false; // Đã phản hồi hay chưa (quản trị dùng)
}
