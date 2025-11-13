package com.example.kimthanhphatmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DocumentController {

    // 🧾 Trang danh sách văn bản
    @GetMapping("/documents")
    public String documentsList() {
        // trỏ tới templates/documents.html
        return "documents";
    }

    // 📘 Trang chi tiết động
    @GetMapping("/documents/{slug}")
    public String documentDetail(@PathVariable("slug") String slug) {
        // Đường dẫn tới templates/documents/[slug].html
        String templatePath = "documents/" + slug;

        // Nếu file slug.html tồn tại → Thymeleaf sẽ render trang đó.
        // Nếu KHÔNG tồn tại → bạn có thể thêm cơ chế fallback sau (ví dụ: 404)
        return templatePath;
    }
}
