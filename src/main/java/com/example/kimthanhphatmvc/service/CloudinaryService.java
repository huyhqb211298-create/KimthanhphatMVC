package com.example.kimthanhphatmvc.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    // Hàm cũ Product đang dùng – GIỮ NGUYÊN
    String uploadFile(MultipartFile file);

    // Hàm mới dùng cho News
    String upload(MultipartFile file);
}
