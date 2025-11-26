package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.repository.*;
import com.example.kimthanhphatmvc.util.SlugUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlugService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 🔎 Kiểm tra slug có tồn tại trong bất kỳ bảng nào chưa
     */
    public boolean existsInAnyTable(String slug) {
        return productRepository.existsBySlug(slug)
                || productTypeRepository.existsBySlug(slug)
                || brandRepository.existsBySlug(slug)
                || categoryRepository.existsBySlug(slug);
    }

    /**
     * 🛠️ Tạo slug duy nhất (tự động thêm -1, -2 nếu trùng)
     */
    public String generateUniqueSlug(String baseSlug) {
        String slug = baseSlug;
        int count = 1;

        while (existsInAnyTable(slug)) {
            slug = baseSlug + "-" + count;
            count++;
        }

        return slug;
    }

    /**
     * 🚀 Hàm tạo slug từ name (dùng khi tạo mới entity)
     */
    public String createSlug(String name) {
        // 1️⃣ Convert tên sang slug cơ bản
        String baseSlug = SlugUtil.toSlug(name);

        // 2️⃣ Tạo slug duy nhất
        return generateUniqueSlug(baseSlug);
    }
}
