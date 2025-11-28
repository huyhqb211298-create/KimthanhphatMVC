package com.example.kimthanhphatmvc.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.kimthanhphatmvc.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        // Product dùng folder products
        return doUpload(file, "kimthanhphat/products");
    }

    @Override
    public String upload(MultipartFile file) {
        // News dùng folder news
        return doUpload(file, "kimthanhphat/news");
    }

    private String doUpload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) return null;

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", folder)
            );
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
        }
    }
}
